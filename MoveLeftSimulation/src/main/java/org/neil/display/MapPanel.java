package org.neil.display;

import org.neil.map.CoordinateMap;
import org.neil.board.Coordinates;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.neural.input.CreatureInputs;
import org.neil.neural.input.Inputs;
import org.neil.neural.output.CreatureOutputs;
import org.neil.object.Creature;
import org.neil.simulation.ReproductionPrioritization;
import org.neil.simulation.Simulation;
import org.neil.simulation.SimulationInput;
import org.neil.simulation.SimulationOutput;
import org.neil.simulation.SurviveHelperFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class MapPanel extends JPanel {
    private CoordinateMap coordinateMap;
    private int gridSize = 5;
    private final int stepDisplayInMillis = 100;

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    Queue<Collection<Coordinates>> frames = new LinkedBlockingQueue<>();

    private Simulation<Coordinates,Creature> simulation;
    private SimulationOutput<Coordinates,Creature> output;
    Supplier<MainFrame> mainFrameUpdater = () -> null;

    Collection<Coordinates> previousFrame = Collections.emptyList();

    public MapPanel() {
        initLeftDestination();
        //initMovingDestination();


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
                List<Map<Coordinates,Creature>> recordings = output.getRun(numOfRuns).getRecording();

                frames.addAll(recordings
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

    public void addFrameListener(Supplier<MainFrame> mainFrameUpdater) {
        this.mainFrameUpdater = mainFrameUpdater;
    }

    public Simulation initLeftDestination(){
        gridSize = 10;

        SimulationInput<Inputs, Creature> simulationInput = new SimulationInput();
        simulationInput.x = 100;
        simulationInput.y = 100;

        simulationInput.maxNumberOfConnections = 100;
        simulationInput.maxNumberOfNodes = 10;

        simulationInput.survivorPriority =  ReproductionPrioritization.xCompare();

        simulationInput.outputNodeGenerator = new CreatureOutputs();

        CoordinateSupplier coordinateSupplier = new CoordinateSupplier(simulationInput.x,simulationInput.y);

        System.out.println(coordinateSupplier.get());

        simulationInput.inputNodeGenerator = new CreatureInputs(coordinateSupplier);
        simulationInput.numberOfElements = x -> 1000;

        this.coordinateMap = new CoordinateMap(simulationInput.x,simulationInput.y);
        simulationInput.surviveLogic = SurviveHelperFunctions.leftMostSurvives();



        this.simulation = new Simulation(simulationInput,
                coordinateMap,
                new RandomNetworkBuilder(simulationInput));

        this.output = new SimulationOutput<>(simulation,
                Creature::getPosition,
                20);

        return simulation;
    }
    public Simulation initMovingDestination(){
        SimulationInput<Inputs, Creature> simulationInput = new SimulationInput();
        simulationInput.outputNodeGenerator = new CreatureOutputs();

        CoordinateSupplier coordinateSupplier = new CoordinateSupplier(simulationInput.x,simulationInput.y);

        System.out.println(coordinateSupplier.get());

        simulationInput.inputNodeGenerator = new CreatureInputs(coordinateSupplier);
        simulationInput.numberOfElements = x -> 2000;
        simulationInput.survivorPriority =  ReproductionPrioritization.euclidianCompare(coordinateSupplier);
        simulationInput.numberOfSurvivors = x -> x.getRunsCompleted() < 600 ? 900 - x.getRunsCompleted() :  300;

        this.coordinateMap = new CoordinateMap(simulationInput.x,simulationInput.y);
        //simulationInput.surviveLogic = SurviveHelperFunctions.leftMostSurvives();
        simulationInput.surviveLogic = (sim,e) -> true;

        this.simulation = new Simulation(simulationInput,
                coordinateMap,
                new RandomNetworkBuilder(simulationInput));
        this.simulation.addRunCompletionListener( x -> {
            coordinateSupplier.random(x.getRunsCompleted()/20);
            System.out.println(coordinateSupplier.get());
        });

        this.output = new SimulationOutput<>(simulation,
                Creature::getPosition,
                20);

        return simulation;
    }

    public static class CoordinateSupplier implements Supplier<Coordinates>{
        private static Random random = new Random();
        private volatile Coordinates coordinates;
        private final int maxX;
        private final int maxY;

        public CoordinateSupplier(int xMax, int yMax) {
            this.coordinates = Coordinates.of(random.nextInt(xMax), random.nextInt(yMax));
            this.maxX = xMax;
            this.maxY = yMax;
        }


        public void random(int distance) {
            int xMin = coordinates.x - distance;
            int xMax = coordinates.x + distance;

            if (xMin < 0) {
                xMin = 0;
            }
            if (xMax > this.maxX) {
                xMax = this.maxX;
            }

            int yMin = coordinates.y - distance;
            int yMax = coordinates.y + distance;

            if (yMin < 0) {
                yMin = 0;
            }
            if (yMax > this.maxY) {
                yMax = this.maxY;
            }

            int x = coordinates.x;
            if (xMin != xMax) {
                x = random.nextInt(xMax - xMin) + xMin;
            }
            int y = coordinates.y;
            if (yMin != yMax) {
                y = random.nextInt(yMax - yMin) + yMin;
            }

            this.coordinates = Coordinates.of(x, y);
        }

        @Override
        public Coordinates get() {
            return coordinates;
        }
    }
}
