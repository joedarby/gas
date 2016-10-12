package data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.nio.channels.Pipe;
import java.util.Date;

public class Pipeline implements Comparable<Pipeline> {

    public String pipelineName;
    public final double flowValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    public final Date timestamp;

    public Pipeline(String name, double flow, Date stamp) {
        pipelineName = name;
        flowValue = flow;
        timestamp = stamp;
    }

    @Override
    public int compareTo(Pipeline another) {
        if (this.flowValue < another.flowValue) {
            return 1;
        } else {
            return -1;
        }

    }

}


