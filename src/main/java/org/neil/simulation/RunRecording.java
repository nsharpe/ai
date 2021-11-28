package org.neil.simulation;

import org.neil.map.Coordinates;
import org.neil.object.Life;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


public class RunRecording {

    private List<Map<Coordinates, Life>> recordings = new ArrayList<>();

    public Map<Coordinates, Life> frame(int i) {
        return recordings.get(i);
    }

    public void createFrame(Collection<? extends Life> frame) {
        recordings.add(frame.stream().collect(Collectors.toMap(x -> x.getPosition(), x -> x)));
    }

    public void stepConsumer(Simulation simulation) {
        createFrame(simulation.getCoordinateMap().getCreatures());
    }
}
