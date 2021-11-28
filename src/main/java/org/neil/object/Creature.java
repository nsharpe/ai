package org.neil.object;

import org.neil.map.CoordinateMap;

import org.neil.neural.Network;
import org.neil.neural.RandomNetworkBuilder;
import org.neil.neural.input.Inputs;

public class Creature extends Life {
    private final Network neuralNetwork;
    private Direction direction = Direction.random();
    private final CoordinateMap coordinateMap;

    public Creature(RandomNetworkBuilder randomNetworkBuilder, CoordinateMap coordinateMap) {
        neuralNetwork = randomNetworkBuilder.build();
        this.coordinateMap = coordinateMap;
    }

    public Creature(Network neuralNetwork, CoordinateMap coordinateMap) {
        this.neuralNetwork = neuralNetwork;
        this.coordinateMap = coordinateMap;
    }

    public CoordinateMap getCoordinateMap() {
        return coordinateMap;
    }

    public Direction getDirection() {
        return direction;
    }

    public void input() {
        Inputs inputs = new Inputs();
        inputs.direction = direction;
        neuralNetwork.getInputs()
                .forEach(x->x.input(inputs));
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Network getNeuralNetwork() {
        return neuralNetwork;
    }

    public void activateNeuralNetworkOutput() {
        neuralNetwork.getOutputs().stream()
                .forEach(x -> x.consumeOutput(this));
    }
}
