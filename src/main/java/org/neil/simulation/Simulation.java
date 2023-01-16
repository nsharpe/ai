package org.neil.simulation;

import org.neil.map.CoordinateMap;
import org.neil.map.Coordinates;
import org.neil.neural.Network;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.object.Creature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * A Run is defined as a completion of steps that represent
 */
public class Simulation {
    private static Random random = new Random();

    private final int runTime;
    private final int numberOfCreatures;
    private final int numberOfRuns;
    private CoordinateMap coordinateMap;
    private Predicate<Creature> acceptanceCriteria = this::defaultAcceptanceCriteria;
    private RandomNetworkBuilder randomNetworkBuilder;
    private final Comparator<Creature> survivorPriority;
    private final int maxNumberOfSurvivors;

    private int runsCompleted = 0;

    private Map<Creature, Coordinates> creatureInitialPosition = new HashMap<>();

    private Consumer<Simulation> stepCompleteListener = x -> { // noop
    };
    private Consumer<Simulation> runCompletionListener = x -> { //noop
        System.out.println("Run #" + runsCompleted + " completed: ");
    };

    public Simulation(SimulationInput simulationInput,
                      CoordinateMap coordinateMap,
                      RandomNetworkBuilder randomNetworkBuilder) {

        this.coordinateMap = Objects.requireNonNull(coordinateMap, "coordinateMap");
        this.randomNetworkBuilder = Objects.requireNonNull(randomNetworkBuilder, "randomNetworkBuilder");

        Objects.requireNonNull(simulationInput, "simulationInput");
        this.runTime = simulationInput.runTime;
        this.numberOfCreatures = simulationInput.numberOfCreatures;
        this.numberOfRuns = simulationInput.numberOfRuns;
        this.maxNumberOfSurvivors = simulationInput.maxNumberOfSurvivors;
        this.survivorPriority = simulationInput.survivorPriority;
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
            coordinateMap.activateAllNeuralNets();
            stepCompleteListener.accept(this);
        }
        runsCompleted++;
        runCompletionListener.accept(this);
    }

    private void createCreatures() {
        createCreatures(numberOfCreatures);
    }

    private void createCreatures(int total) {
        for (int i = 0; i < total; i++) {
            coordinateMap.generateCreature(randomNetworkBuilder.build());
        }
    }

    private void resetRun() {
        // copy neural networks that match criteria
        List<Network> networks = coordinateMap.getCreatures()
                .stream()
                .filter(acceptanceCriteria)
                .sorted(survivorPriority)
                .map(x -> x.getNeuralNetwork())
                .limit(maxNumberOfSurvivors)
                .collect(Collectors.toCollection(() -> new ArrayList<>()));

        //kill all creatures
        coordinateMap.clearMap();

        // create creatures based off of neural networks
        if (networks.isEmpty()) {
            // in the event that all creatures died create random creatures
            createCreatures();
        } else {
            IntStream.range(0, numberOfCreatures)
                    .mapToObj(x -> networks.get(x % networks.size()))
                    .parallel()
                    .map(x -> randomNetworkBuilder.copyWithChanceToMutate(x))
                    .collect(Collectors.toList())
                    .stream()
                    .forEach(x -> coordinateMap.generateCreature(x));
        }
        creatureInitialPosition.clear();
        creatureInitialPosition.putAll(coordinateMap.getCreatures().stream()
                .collect(Collectors.toMap(x -> x, x -> x.getPosition())));
    }

    public CoordinateMap getCoordinateMap() {
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

    private boolean defaultAcceptanceCriteria(Creature creature) {
        return creatureOnLeftSide(creature)
                // && creatureHasMoved(creature)
                //&& extinctionEvent(creature)
                ;
    }

    private boolean creatureHasMoved(Creature creature){
        return !creature.getPosition().equals(creatureInitialPosition.get(creature));
    }

    private boolean creatureOnLeftSide(Creature creature) {
        return creature.getPosition().x <= coordinateMap.xRange / 2;
    }


    private boolean extinctionEvent(Creature creature) {
        //extinction event
        if (runsCompleted % 100 == 99) {
            if (creature.getPosition().x >= 10) {
                return false;
            }
        }
        return true;
    }
}
