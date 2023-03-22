package org.neil.simulation;

import org.neil.neural.NetworkOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationOutput<K,E extends NetworkOwner> {
    private List<RunRecording> recordings = new ArrayList<>();
    private Simulation<K,E> simulation;
    private final int maxRuns;
    private AtomicInteger numberOfRuns = new AtomicInteger();

    public SimulationOutput(Simulation<K,E> simulation, int maxRuns) {
        this.maxRuns = maxRuns;
        this.simulation = Objects.requireNonNull(simulation, "simulation");
        recordings.add(new RunRecording());
        numberOfRuns.getAndIncrement();

        simulation.setRunCompletionListener(x -> {
            synchronized (recordings) {
                recordings.add(new RunRecording());
                numberOfRuns.getAndIncrement();
                if (maxRuns > 0 && recordings.size() > maxRuns) {
                    recordings.remove(0);
                }
            }
        });

        simulation.setStepCompleteListener(x -> recordings.get(recordings.size() - 1).stepConsumer(x));
    }

    public int numberOfRuns() {
        return numberOfRuns.get();
    }

    public RunRecording getRun(int i) {
        synchronized (recordings) {
            if (i < numberOfRuns() - recordings.size()) {
                throw new IllegalStateException("Missing recording");
            }
            int base = numberOfRuns() - recordings.size();
            return recordings.get(i - base - 1);
        }
    }

}
