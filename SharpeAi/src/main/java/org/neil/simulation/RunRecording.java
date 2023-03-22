package org.neil.simulation;

import org.neil.neural.NetworkOwner;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


public class RunRecording<K,V extends NetworkOwner> {

    private List<Map<K, V>> recordings = new ArrayList<>();

    private Function<V,K> mapper;

    public RunRecording(Function<V, K> mapper) {
        this.mapper = Objects.requireNonNull(mapper);
    }

    public Map<K, V> frame(int i) {
        return recordings.get(i);
    }

    public void createFrame(Collection<? extends V> frame) {
        recordings.add(frame.stream().collect(Collectors.toMap(mapper, Function.identity())));
    }

    public void stepConsumer(Simulation simulation) {
        createFrame(simulation.getSimulationEnvironment().getValues());
    }

    public List<Map<K,V>> getRecording(){
        return recordings.stream()
                .map( x -> Collections.unmodifiableMap(x))
                .toList();
    }

    public Map<K,V> getRecording(int index){
        return Collections.unmodifiableMap(recordings.get(index));
    }
}
