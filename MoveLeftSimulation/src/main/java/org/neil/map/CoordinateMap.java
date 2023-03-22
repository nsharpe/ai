package org.neil.map;

import org.neil.neural.Network;
import org.neil.object.Creature;
import org.neil.simulation.SimulationEnvironment;

import java.util.*;
import java.util.stream.Collectors;

public class CoordinateMap implements SimulationEnvironment<Coordinates,Creature> {
    public final int xRange;
    public final int yRange;

    private static final Random RANDOM = new Random();

    private Map<Coordinates, Creature> creatureList = Collections.synchronizedMap(new HashMap<>());
    private Map<Coordinates, Creature> nextMapStep = Collections.synchronizedMap(new HashMap<>());

    public CoordinateMap(int xRange, int yRange) {
        this.xRange = xRange;
        this.yRange = yRange;
    }

    public boolean isEmpty(Coordinates coordinates) {
        return !creatureList.containsKey(coordinates);
    }

    public void move(Creature creature, Coordinates newPosition) {
        if (isEmpty(newPosition)
                && !nextMapStep.containsKey(newPosition)
                && newPosition.x >= 0 && newPosition.x < xRange
                && newPosition.y >= 0 && newPosition.y < yRange) {
            nextMapStep.remove(creature.getPosition());
            creature.setPosition(newPosition);
            nextMapStep.put(newPosition, creature);
        }
    }

    public void incrementMapMovements() {
        creatureList.clear();
        creatureList.putAll(nextMapStep);
    }

    @Override
    public void preStepAction() {
        creatureList.values().stream()
                .parallel()
                .forEach(x -> {
                    synchronized (x) {
                        x.input();
                    }
                });
    }

    @Override
    public void postStepAction() {
        //set output intentions
        creatureList.values().stream()
                .parallel()
                .forEach(x -> x.activateNeuralNetworkOutput());

        incrementMapMovements();
    }

    public void generateCreature(Network neuralNetwork) {
        Creature creature = new Creature(neuralNetwork, this);
        creature.setPosition(randomEmpty());
        addCreature(creature);
    }

    public Coordinates randomEmpty() {
        Coordinates toReturn;
        do {
            toReturn = Coordinates.of(RANDOM.nextInt(xRange), RANDOM.nextInt(yRange));
        } while (!isEmpty(toReturn));
        return toReturn;
    }

    public void kill(Creature creature) {
        this.creatureList.remove(creature.getPosition());
        this.nextMapStep.remove(creature.getPosition());
    }

    public void clearMap() {
        this.creatureList.clear();
        this.nextMapStep.clear();
    }

    @Override
    public void addElement(Network neuralNetwork) {
        generateCreature(neuralNetwork);
    }

    public void addCreature(Creature creature) {
        creatureList.put(creature.getPosition(), creature);
        nextMapStep.put(creature.getPosition(), creature);
    }

    public Collection<Creature> getCreatures() {
        return creatureList.values();
    }

    @Override
    public Collection getValues() {
        return getCreatures();
    }

    public void generationEnds(){
        clearMap();
    }
}
