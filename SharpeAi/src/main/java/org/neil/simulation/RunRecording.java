package org.neil.simulation;

import org.neil.map.Coordinates;
import org.neil.neural.NetworkOwner;
import org.neil.object.Creature;
import org.neil.object.Life;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


public class RunRecording {

    private List<Map<Coordinates, Creature>> recordings = new ArrayList<>();

    public Map<Coordinates, Creature> frame(int i) {
        return recordings.get(i);
    }

    public void createFrame(Collection<? extends Creature> frame) {
        recordings.add(frame.stream().collect(Collectors.toMap(x -> x.getPosition(), x -> x)));
    }

    public void stepConsumer(Simulation simulation) {
        createFrame(simulation.getCoordinateMap().getValues());
    }

    public List<Map<Coordinates,Creature>> getRecording(){
        return recordings.stream()
                .map( x -> Collections.unmodifiableMap(x))
                .toList();
    }
}
