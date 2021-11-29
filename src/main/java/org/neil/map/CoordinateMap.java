package org.neil.map;

import org.neil.neural.Network;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.object.Creature;

import java.util.*;

public class CoordinateMap {
    public final int xRange;
    public final int yRange;

    private static final Random RANDOM = new Random();

    private Map<Coordinates, Creature> creatureList = new HashMap<>();
    private Map<Coordinates, Creature> nextMapStep = new HashMap<>();

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
            creature.setPosition(newPosition);
            nextMapStep.remove(creature.getPosition());
            nextMapStep.put(newPosition, creature);
        }
    }

    public void incrementMapMovements() {
        creatureList.clear();
        creatureList.putAll(nextMapStep);
    }

    public void activateAllNeuralNets() {

        // Set inputs for neural net
        creatureList.values().stream()
                .forEach(x -> x.input());

        // activate neural net
        creatureList.values().stream()
                .map(x -> x.getNeuralNetwork())
                .forEach(x -> x.increment());

        //set output intentions
        creatureList.values().stream()
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

    public void kill(Creature creature){
        this.creatureList.remove(creature.getPosition());
        this.nextMapStep.remove(creature.getPosition());
    }

    public void clearMap(){
        this.creatureList.clear();
        this.nextMapStep.clear();
    }

    public void addCreature(Creature creature) {
        creatureList.put(creature.getPosition(), creature);
        nextMapStep.put(creature.getPosition(), creature);
    }

    public Collection<Creature> getCreatures() {
        return creatureList.values();
    }
}
