package org.neil.simulation;


import org.neil.neural.Network;
import org.neil.neural.NetworkOwner;
import org.neil.neural.RandomNetworkBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 */
public class Simulation<K,E extends NetworkOwner> {
    private static Random random = new Random();

    private final int runTime;
    private final Function<Simulation,Integer> numberOfCreatures;
    private final int numberOfRuns;
    private SimulationEnvironment<K,E> coordinateMap;
    private final Predicate<E> acceptanceCriteria;
    private RandomNetworkBuilder randomNetworkBuilder;
    private final Comparator<E> survivorPriority;

    private int runsCompleted = 0;

    private Consumer<Simulation> stepCompleteListener = x -> { // noop
    };
    private Consumer<Simulation> runCompletionListener;

    private final Function<Simulation,Integer> numberOfSurvivors;

    public Simulation(SimulationInput simulationInput,
                      SimulationEnvironment coordinateMap,
                      RandomNetworkBuilder randomNetworkBuilder) {

        this.coordinateMap = Objects.requireNonNull(coordinateMap, "coordinateMap");
        this.randomNetworkBuilder = Objects.requireNonNull(randomNetworkBuilder, "randomNetworkBuilder");

        Objects.requireNonNull(simulationInput, "simulationInput");
        this.runTime = simulationInput.runTime;
        this.numberOfCreatures = simulationInput.numberOfCreatures;
        this.numberOfRuns = simulationInput.numberOfRuns;
        this.survivorPriority = simulationInput.survivorPriority;
        this.acceptanceCriteria = x -> simulationInput.surviveLogic.survives(this, x);
        this.numberOfSurvivors = simulationInput.numberOfSurvivors;

        this.runCompletionListener = x -> { //noop
            System.out.println("Run #" + runsCompleted + " completed:- NumberOfSurvivors:" + numberOfSurvivors.apply(x));
        };
    }

    public void start() {
        try {
            for (int i = 0; i < numberOfRuns; i++) {
                startRun();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public void startRun() {
        resetRun();
        for (int i = 0; i < runTime; i++) {
            coordinateMap.preStepAction();
            coordinateMap.getValues()
                    .stream()
                    .parallel()
                    .forEach(x -> x.getNeuralNetwork().increment());
            coordinateMap.postStepAction();
            stepCompleteListener.accept(this);
        }
        runsCompleted++;
        runCompletionListener.accept(this);
    }

    private void createElements() {
        createElements(numberOfCreatures.apply(this));
    }

    private void createElements(int total) {
        for (int i = 0; i < total; i++) {
            coordinateMap.addElement(randomNetworkBuilder.build());
        }
    }

    private void resetRun() {
        // copy neural networks that match criteria
        List<Network> networks = coordinateMap.getValues()
                .stream()
                .filter(acceptanceCriteria)
                .sorted(survivorPriority)
                .map(x -> x.getNeuralNetwork())
                .limit(numberOfSurvivors.apply(this))
                .collect(Collectors.toCollection(() -> new ArrayList<>()));

        //kill all creatures
        coordinateMap.endRun();

        // create creatures based off of neural networks
        if (networks.isEmpty()) {
            // in the event that all creatures died create random creatures
            createElements();
        } else {
            IntStream.range(0, numberOfCreatures.apply(this))
                    .mapToObj(x -> networks.get(x % networks.size()))
                    .parallel()
                    .map(x -> randomNetworkBuilder.copyWithChanceToMutate(x))
                    .collect(Collectors.toList())
                    .stream()
                    .forEach(x -> coordinateMap.addElement(x));
        }
    }

    public SimulationEnvironment<K, E> getCoordinateMap() {
        return coordinateMap;
    }

    public void setStepCompleteListener(Consumer<Simulation> stepCompleteListener) {
        this.stepCompleteListener = this.stepCompleteListener.andThen(stepCompleteListener);
    }

    public void setRunCompletionListener(Consumer<Simulation> runCompletionListener) {
        this.runCompletionListener = this.runCompletionListener.andThen(runCompletionListener);
    }

    public int getRunsCompleted() {
        return runsCompleted;
    }

}
