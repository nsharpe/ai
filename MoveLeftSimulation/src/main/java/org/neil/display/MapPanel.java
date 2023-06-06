package org.neil.display;

import org.neil.map.CoordinateMap;
import org.neil.board.Coordinates;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.neural.input.CreatureInputs;
import org.neil.neural.input.Inputs;
import org.neil.neural.output.CreatureOutputs;
import org.neil.object.Creature;
import org.neil.simulation.MutationStrategy;
import org.neil.simulation.ReproductionPrioritization;
import org.neil.simulation.Simulation;
import org.neil.simulation.SimulationInput;
import org.neil.simulation.SimulationOutput;
import org.neil.simulation.SurviveHelperFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;
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
import java.util.stream.Stream;

public class MapPanel extends JPanel {
    private CoordinateMap coordinateMap;
    private int gridSize = 5;
    private static final int STEP_DISPLAY_IN_MILLIS = 100;

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    Queue<Collection<Coordinates>> frames = new LinkedBlockingQueue<>();

    private Simulation<Coordinates,Creature> simulation;
    private SimulationOutput<Coordinates,Creature> output;
    Supplier<MainFrame> mainFrameUpdater = () -> null;

    Collection<Coordinates> previousFrame = Collections.emptyList();

    public MapPanel() {
        //initLeftDestination();
        initMovingDestination();


        ExecutorService simulationThread = Executors.newSingleThreadExecutor();

        simulationThread.submit(() -> simulation.start());

        executor.scheduleAtFixedRate(() -> {
            this.removeAll();
            this.revalidate();
            this.repaint();
        }, STEP_DISPLAY_IN_MILLIS, STEP_DISPLAY_IN_MILLIS, TimeUnit.MILLISECONDS);
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
                        .map(Map::keySet)
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

        simulationInput.survivorPriority =  ReproductionPrioritization.xCompare();

        simulationInput.outputNodeGenerator = new CreatureOutputs();

        simulationInput.inputNodeGenerator = new CreatureInputs();
        simulationInput.numberOfElements = x -> 2000;
        simulationInput.mutationStrategy = MutationStrategy.ALWAYS_ALLOW;


        this.coordinateMap = new CoordinateMap(simulationInput.x,simulationInput.y);
        simulationInput.surviveLogic = SurviveHelperFunctions.leftMostSurvives();

        RandomNetworkBuilder randomNetworkBuilder = new RandomNetworkBuilder(simulationInput)
                .maxNodes(10)
                .mutationRate(0.1)
                .minConnection(1)
                .maxConnection(100)
                .minBandwith(1)
                .maxBandwith(2048)
                .minNodes(1)
                .maxStorage(600);

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

        CoordinateSupplier coordinateSupplier = new CoordinateSupplier(
                Coordinates.of(0, 100),
                simulationInput.x,
                simulationInput.y);

        simulationInput.inputNodeGenerator = new CreatureInputs(coordinateSupplier);
        simulationInput.numberOfElements = x -> 3000;
        simulationInput.survivorPriority =  new TimedComparator(coordinateSupplier,100);
        simulationInput.numberOfSurvivors = x -> x.getRunsCompleted() % 100 > 50 ? 100 : 1000;

        this.coordinateMap = new CoordinateMap(simulationInput.x,simulationInput.y);
        simulationInput.surviveLogic = (sim,x) -> ((Creature)x).hasMoved();

        RandomNetworkBuilder randomNetworkBuilder = new RandomNetworkBuilder(simulationInput)
                .maxConnection(20)
                .minConnection(10)
                .minNodes(7)
                .maxNodes(10)
                .maxActivation(2048)
                .minBandwith(1)
                .maxBandwith(2048)
                .mutationRate(0.75)
                .minStorage(100)
                .maxStorage(10000);

        this.simulation = new Simulation(simulationInput,
                coordinateMap,
                randomNetworkBuilder);

        System.out.println(coordinateSupplier.get());

        this.simulation.addRunCompletionListener( x -> coordinateSupplier.runCompleteRun(x));

        this.simulation.addRunCompletionListener( x -> {
            if(simulation.getRunsCompleted() % 10 == 0) {
                int avgNodes = (int)simulation.getSimulationEnvironment()
                        .getValues()
                        .stream()
                        .mapToInt(y->y.getNeuralNetwork().getIntermediateNodes().size())
                        .average()
                        .getAsDouble();

                int newMaxNodes = (int) (avgNodes * 1.8);
                newMaxNodes = newMaxNodes < 200 ? newMaxNodes : 200;

                int minNodes = (int) (avgNodes * 0.5);
                minNodes = minNodes < 7 ? 7 : minNodes;

                int newMinConnection = (int)(simulation.getSimulationEnvironment().getValues()
                        .stream()
                        .map( y -> y.getNeuralNetwork())
                        .mapToLong( y -> y.streamConnections().count())
                        .average().getAsDouble() * 0.5);

                int newMaxConnection = (int)(simulation.getSimulationEnvironment().getValues()
                        .stream()
                        .map( y -> y.getNeuralNetwork())
                        .mapToLong( y -> y.streamConnections().count())
                        .average().getAsDouble() * 1.5);
                newMaxConnection = newMaxConnection < 2000 ? newMaxConnection : 2000;

                newMinConnection = newMinConnection < 10 ? 10 : newMinConnection;

                randomNetworkBuilder.maxConnection(newMaxConnection)
                        .minConnection(minNodes)
                        .minConnection(newMinConnection)
                        .maxNodes(newMaxNodes);

            }

            if(simulation.getRunsCompleted() > 2000) {
                coordinateSupplier.random(Math.abs((int) (Math.sin(((double) simulation.getRunsCompleted() / 1000.0)) * 20)));
            }

            System.out.println(coordinateSupplier.get());
            reportStats(x);
        });

        this.output = new SimulationOutput<>(simulation,
                Creature::getPosition,
                20);

        return simulation;
    }

    private static void reportStats(Simulation<Coordinates,Creature> simulation){
        IntSummaryStatistics nodeSummaryStatistics = simulation.getSimulationEnvironment()
                .getValues().stream()
                .mapToInt(x->x.getNeuralNetwork().getIntermediateNodes().size())
                .summaryStatistics();

        LongSummaryStatistics connectionStats = simulation.getSimulationEnvironment()
                .getValues().stream()
                .mapToLong(x->x.getNeuralNetwork().streamConnections().count())
                .summaryStatistics();

        System.out.println("nodeStats:"+ nodeSummaryStatistics
                + " connectionStats:" + connectionStats);
    }


    private static void setMinToCurrentMin(RandomNetworkBuilder rnb, Simulation simulation){
        Stream<Creature> creatureStream = simulation.getSimulationEnvironment().getValues().stream();
        int minConnection = (int)creatureStream.mapToLong(c -> c.getNeuralNetwork()
                .streamConnections().count()).min().getAsLong();

        //rnb.minConnection(minConnection);

        creatureStream = simulation.getSimulationEnvironment().getValues().stream();
        int minNode = creatureStream.mapToInt(c -> c.getNeuralNetwork()
                .getIntermediateNodes().size()).min().getAsInt();

        //rnb.minNodes(minNode);

        System.out.println("Setting minConnection:" + minConnection + " minNode:"+minNode);
    }

    public class TimedComparator implements Comparator<Creature>{
        int switchOver;
        CoordinateSupplier coordinateSupplier;

        public TimedComparator( CoordinateSupplier coordinateSupplier,int switchOver) {
            this.switchOver = switchOver;
            this.coordinateSupplier = coordinateSupplier;

        }

        @Override
        public int compare(Creature o1, Creature o2) {
            Coordinates destination = coordinateSupplier.get();
            return score(o1,destination) - score(o2,destination);
        }

        private int score(Creature creature, Coordinates destination) {
            if( simulation.getRunsCompleted() > switchOver) {
                return (int) creature.getPosition().distance(destination);
            }
            return Math.abs(destination.x - creature.getPosition().x);
        }
    }

    public class CoordinateSupplier implements Supplier<Coordinates>{
        private static Random random = new Random();
        private volatile Coordinates coordinates;
        private final int maxX;
        private final int maxY;

        public CoordinateSupplier(Coordinates init, int xMax, int yMax) {
            this.coordinates = init;
            this.maxX = xMax;
            this.maxY = yMax;
        }

        public CoordinateSupplier(int xMax, int yMax) {
            this( Coordinates.of(random.nextInt(xMax), random.nextInt(yMax)), xMax,yMax);
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

        public int randomY(){
            return random.nextInt(maxY) ;
        }

        public int randomX(){
            return random.nextInt(maxX);
        }

        public void runCompleteRun(Simulation simulation){
            int x = 20;
            if(simulation.getRunsCompleted() > 200 && random.nextBoolean()){
                x = 50;
            }
            int y;
            if(simulation.getRunsCompleted() > 400){
                y = random.nextInt(maxY / 2) + maxY / 4;
            }else {
                y = random.nextBoolean() ? 0 : maxY;
            }
            coordinates = Coordinates.of(x,randomY());

        }

        @Override
        public Coordinates get() {
            return coordinates;
        }
    }
}
