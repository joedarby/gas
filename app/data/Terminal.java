package data;

import java.util.ArrayList;

/**
 * Created by Joe on 03/09/2016.
 */
public class Terminal {
    public String terminalName;
    public String terminalTimestamp;
    public double terminalFlow = 0.0;
    public ArrayList<Pipeline> pipelines = new ArrayList<>();

    public Terminal(String name) {
        terminalName = name;
    }

    public Terminal addPipeline(Pipeline pipe) {
        pipelines.add(pipe);
        terminalTimestamp = pipelines.get(0).timestamp;
        terminalFlow = terminalFlow + pipe.flowValue;
        return this;
    }
}