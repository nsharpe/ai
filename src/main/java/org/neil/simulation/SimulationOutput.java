package org.neil.simulation;

import org.neil.map.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class SimulationOutput {
    private List<RunRecording> recordings = new ArrayList<>();
    private Simulation simulation;

    public SimulationOutput(Simulation simulation) {
        this.simulation = simulation;

        simulation.setRunCompletionListener( x -> recordings.add(new RunRecording()));
        simulation.setStepCompleteListener( x -> recordings.get(recordings.size()-1).stepConsumer(x));
    }

    public int numberOfRuns(){
        return recordings.size();
    }

    public RunRecording getRun(int i ){
        return recordings.get(i);
    }

}
