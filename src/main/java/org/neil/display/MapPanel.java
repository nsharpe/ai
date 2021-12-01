package org.neil.display;

import org.neil.map.CoordinateMap;
import org.neil.map.Coordinates;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.object.Creature;
import org.neil.simulation.Simulation;
import org.neil.simulation.SimulationInput;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapPanel extends JPanel {
    private final CoordinateMap coordinateMap;
    private final int gridSize = 10;

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    Queue<List<Coordinates>> frames = new LinkedBlockingQueue<>();

    Simulation simulation;

    List<Coordinates> previousFrame = Collections.emptyList();

    public MapPanel() {
        this(new CoordinateMap(150, 150));
        this.setPreferredSize(new Dimension(coordinateMap.xRange * gridSize,
                coordinateMap.yRange * gridSize));
    }

    public MapPanel(CoordinateMap coordinateMap) {
        this.coordinateMap = Objects.requireNonNull(coordinateMap, "coordinateMap");

        this.simulation = new Simulation(new SimulationInput(),
                coordinateMap,
                new RandomNetworkBuilder());

        ExecutorService simulationThread = Executors.newSingleThreadExecutor();
        this.simulation.setStepCompleteListener(x -> {
            if(x.getRunsCompleted() % 50 != 0){
                return;
            }

            List<Coordinates> frame = x.getCoordinateMap()
                    .getCreatures()
                    .stream()
                    .map(Creature::getPosition)
                    .collect(Collectors.toList());
            frames.offer(frame);

        });

        simulationThread.submit(() -> simulation.start());

        executor.scheduleAtFixedRate(() -> {
            this.removeAll();
            this.revalidate();
            this.repaint();
        }, 50, 50, TimeUnit.MILLISECONDS);

    }

    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        drawGrid(graphics);
        drawMidPointLine(graphics);

        List<Coordinates> frameToUse = frames.poll();


        frameToUse = frameToUse == null ? previousFrame : frameToUse;
        previousFrame = frameToUse;

        for (Coordinates coordinates : frameToUse) {
            populateMap(graphics, coordinates);
        }
    }

    private void drawMidPointLine(Graphics2D graphics2D){
        int x = coordinateMap.xRange / 2;
        graphics2D.setColor(Color.gray);
        IntStream.range(0,coordinateMap.yRange)
                .forEach( y -> populateMap(graphics2D, Coordinates.of(x,y)));
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
}
