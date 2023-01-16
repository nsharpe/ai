package org.neil.display;

import org.neil.map.CoordinateMap;
import org.neil.map.Coordinates;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.object.Creature;
import org.neil.simulation.Simulation;
import org.neil.simulation.SimulationInput;
import org.neil.simulation.SimulationOutput;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class MapPanel extends JPanel {
    private final CoordinateMap coordinateMap;
    private final int gridSize = 10;
    private final int stepDisplayInMillis = 50;

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    Queue<Collection<Coordinates>> frames = new LinkedBlockingQueue<>();

    private final Simulation simulation;
    private final SimulationOutput output;
    Supplier<MainFrame> mainFrameUpdater = () -> null;

    Collection<Coordinates> previousFrame = Collections.emptyList();

    public MapPanel() {
        SimulationInput simulationInput = new SimulationInput();

        this.coordinateMap = new CoordinateMap(simulationInput.x,simulationInput.y);

        this.simulation = new Simulation(new SimulationInput(),
                coordinateMap,
                new RandomNetworkBuilder());
        this.output = new SimulationOutput(simulation, 20);

        ExecutorService simulationThread = Executors.newSingleThreadExecutor();

        simulationThread.submit(() -> simulation.start());

        executor.scheduleAtFixedRate(() -> {
            this.removeAll();
            this.revalidate();
            this.repaint();
        }, stepDisplayInMillis, stepDisplayInMillis, TimeUnit.MILLISECONDS);
        this.setPreferredSize(new Dimension(coordinateMap.xRange * gridSize,
                coordinateMap.yRange * gridSize));
    }

    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        drawGrid(graphics);
        drawMidPointLine(graphics);

        if(frames.isEmpty()){
            int numOfRuns = output.numberOfRuns()-1;
            if(numOfRuns > 0) {
                frames.addAll(output.getRun(numOfRuns).getRecording()
                        .stream()
                        .map(x->x.keySet())
                .toList());

                mainFrameUpdater.get().setTitle("RUN: " + numOfRuns);
            }
        }
        Collection<Coordinates> frameToUse = frames.poll();


        frameToUse = frameToUse == null ? previousFrame : frameToUse;
        previousFrame = frameToUse;

        for (Coordinates coordinates : frameToUse) {
            populateMap(graphics, coordinates);
        }
    }

    private void drawMidPointLine(Graphics2D graphics2D) {
        int x = coordinateMap.xRange / 2;
        graphics2D.setColor(Color.gray);
        IntStream.range(0, coordinateMap.yRange)
                .forEach(y -> populateMap(graphics2D, Coordinates.of(x, y)));
        graphics2D.setColor(Color.black);

    }

    private void drawGrid(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1));
        int yMax = coordinateMap.yRange * gridSize;
        for (int i = 0; i < coordinateMap.xRange; i++) {
            graphics.drawLine(i * gridSize, 0,
                    i * gridSize, yMax);
        }

        int xMax = coordinateMap.xRange * gridSize;
        for (int i = 0; i < coordinateMap.yRange; i++) {
            graphics.drawLine(0, i * gridSize,
                    xMax, i * gridSize);
        }
    }

    private void populateMap(Graphics2D graphics, Coordinates coordinates) {
        graphics.fillRect(gridSize * coordinates.x,
                gridSize * coordinates.y,
                gridSize,
                gridSize);
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void addFrameListener(Supplier<MainFrame> mainFrameUpdater) {
        this.mainFrameUpdater = mainFrameUpdater;
    }
}
