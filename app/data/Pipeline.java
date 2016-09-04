package data;


public class Pipeline {

    public String pipelineName;
    public double flowValue;
    public String timestamp;

    public Pipeline(String name, double flow, String stamp) {
        pipelineName = name;
        flowValue = flow;
        timestamp = stamp;
    }

}


