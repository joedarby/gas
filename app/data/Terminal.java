package data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Joe on 03/09/2016.
 */
@SuppressWarnings("WeakerAccess")
public class Terminal implements Comparable<Terminal> {
    public String terminalName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    public Date terminalTimestamp;
    public double terminalFlow = 0.0;
    public final ArrayList<Pipeline> pipelines = new ArrayList<>();


    @Override
        public int compareTo(Terminal another) {
            if (this.terminalFlow < another.terminalFlow) {
                return 1;
            } else {
                return -1;
            }
    }

    public Terminal(String name) {
        terminalName = name;
    }

    public void addPipeline(Pipeline pipe) {
        if (TerminalHelper.PIPELINE_TRANSLATE.containsKey(pipe.pipelineName)) {
            pipe.pipelineName = TerminalHelper.PIPELINE_TRANSLATE.get(pipe.pipelineName);
        }
        pipelines.add(pipe);
        terminalTimestamp = pipelines.get(0).timestamp;
        terminalFlow = terminalFlow + pipe.flowValue;
    }
}