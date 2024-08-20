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
    private final Function<Simulation,Integer> numberOfElementsForGeneration;
    private final int numberOfRuns;
    private SimulationEnvironment<K,E> simulationEnvironment;
    private final Predicate<E> acceptanceCriteria;
    private RandomNetworkBuilder randomNetworkBuilder;
    private final Comparator<E> survivorPriority;

    private int runsCompleted = 0;

    private Consumer<Simulation<K,E>> stepCompleteListener = x -> { //noop
    };
    private Consumer<Simulation> runCompletionListener = x -> { //noop
    };

    private final Function<Simulation,Integer> numberOfSurvivors;

    private MutationStrategy mutationStrategy;
    private int mutationAttempts = 10;

    public Simulation(SimulationInput simulationInput,
                      SimulationEnvironment simulationEnvironment,
                      RandomNetworkBuilder randomNetworkBuilder) {

        this.simulationEnvironment = Objects.requireNonNull(simulationEnvironment, "simulationEnvironment");
        this.randomNetworkBuilder = Objects.requireNonNull(randomNetworkBuilder, "randomNetworkBuilder");
        this.mutationStrategy = simulationInput.mutationStrategy;

        Objects.requireNonNull(simulationInput, "simulationInput");
        this.runTime = simulationInput.runTime;
        this.numberOfElementsForGeneration = simulationInput.numberOfElements;
        this.numberOfRuns = simulationInput.numberOfRuns;
        this.survivorPriority = simulationInput.survivorPriority;
        this.acceptanceCriteria = x -> simulationInput.surviveLogic.survives(simulationEnvironment, x);
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
            simulationEnvironment.preStepAction();
            simulationEnvironment.getValues()
                    .stream()
                    .parallel()
                    .forEach(x -> x.getNeuralNetwork().increment());
            simulationEnvironment.getValues()
                            .stream()
                                    .forEach(x -> x.getNeuralNetwork().getIntermediateNodes().forEach(y->y.depreciate()));
            simulationEnvironment.postStepAction();
            stepCompleteListener.accept(this);
        }
        runsCompleted++;
        runCompletionListener.accept(this);
    }

    private void createElements() {
        createElements(numberOfElementsForGeneration.apply(this));
    }

    private void createElements(int total) {
        for (int i = 0; i < total; i++) {
            simulationEnvironment.addElement(randomNetworkBuilder.build());
        }
    }

    private void resetRun() {
        // copy neural networks that match criteria
        List<Network> networks = simulationEnvironment.getValues()
                .stream()
                .filter(acceptanceCriteria)
                .sorted(survivorPriority)
                .map(x -> x.getNeuralNetwork())
                .limit(numberOfSurvivors.apply(this))
                .collect(Collectors.toCollection(() -> new ArrayList<>()));

        simulationEnvironment.generationEnds();

        // create elements based off of neural networks
        if (networks.isEmpty()) {
            // in the event that all elements did not pass criteria create random elements
            createElements();
        } else {
            IntStream.range(0, numberOfElementsForGeneration.apply(this))
                    .mapToObj(x -> {
                        Network network = networks.get(x % networks.size());

                        //  Allows stabilility with high mutation rates
                        if(mutationStrategy == MutationStrategy.WEIGHTS_ONLY && x > networks.size()){
                            return randomNetworkBuilder.copyWithChanceToMutate(network, RandomNetworkBuilder.MutationType.NONE);
                        }
                        return randomNetworkBuilder.copyWithChanceToMutate(network);
                    })
                    .parallel()
                    .collect(Collectors.toList())
                    .stream()
                    .forEach(x -> simulationEnvironment.addElement(x));
        }
    }

    public SimulationEnvironment<K, E> getSimulationEnvironment() {
        return simulationEnvironment;
    }

    public void setStepCompleteListener(Consumer<Simulation> stepCompleteListener) {
        this.stepCompleteListener = this.stepCompleteListener.andThen(stepCompleteListener);
    }

    public void addRunCompletionListener(Consumer<Simulation> runCompletionListener) {
        this.runCompletionListener = this.runCompletionListener.andThen(runCompletionListener);
    }

    public int getRunsCompleted() {
        return runsCompleted;
    }

}
