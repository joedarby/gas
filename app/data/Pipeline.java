package data;

import java.util.Date;

public class Pipeline {

    public String pipelineName;
    public final double flowValue;
    public final Date timestamp;

    public Pipeline(String name, double flow, Date stamp) {
        pipelineName = name;
        flowValue = flow;
        timestamp = stamp;
    }

}


