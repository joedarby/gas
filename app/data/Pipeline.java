package data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Pipeline {

    public String pipelineName;
    public final double flowValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    public final Date timestamp;

    public Pipeline(String name, double flow, Date stamp) {
        pipelineName = name;
        flowValue = flow;
        timestamp = stamp;
    }

}


