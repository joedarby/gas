package data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joe on 03/09/2016.
 */
public class Terminal {
    public String terminalName;
    public String terminalTimestamp;
    public double terminalFlow = 0.0;
    public ArrayList<Pipeline> pipelines = new ArrayList<>();
    private static HashMap pipelineTranslate = new HashMap();
    static {
        pipelineTranslate.put("BACTON BBL", "Bacton BBL");
        pipelineTranslate.put("BACTON IC", "Interconnector");
        pipelineTranslate.put("BACTON PERENCO", "Bacton Perenco");
        pipelineTranslate.put("BACTON SEAL", "Bacton SEAL");
        pipelineTranslate.put("BACTON SHELL", "Bacton Shell");
        pipelineTranslate.put("ST FERGUS MOBIL", "St Fergus Mobil");
        pipelineTranslate.put("ST FERGUS NSMP", "St Fergus NSMP");
        pipelineTranslate.put("ST FERGUS SHELL", "St Fergus Shell");
        pipelineTranslate.put("TEESSIDE PX", "Teesside PX");
        pipelineTranslate.put("TEESSIDE BP", "Teesside CATS");
        pipelineTranslate.put("ALDBROUGH", "Aldbrough");
        pipelineTranslate.put("HILLTOP", "Hilltop");
        pipelineTranslate.put("HOLE HOUSE FARM", "Hole House Farm");
        pipelineTranslate.put("HOLFORD", "Holford");
        pipelineTranslate.put("HORNSEA", "Hornsea");
        pipelineTranslate.put("STUBLACH", "Stublach");
        pipelineTranslate.put("GRAIN NTS 1", "Grain NTS 1");
        pipelineTranslate.put("GRAIN NTS 2", "Grain NTS 2");
        pipelineTranslate.put("EASINGTON DIMLINGTON", "Easington Dimlington");
        pipelineTranslate.put("EASINGTON LANGELED", "Easington Langeled");
        pipelineTranslate.put("AVONMOUTH", "Avonmouth");
        pipelineTranslate.put("GLENMAVIS", "Glenmavis");
        pipelineTranslate.put("DYNEVOR ARMS", "Dynevor Arms");
        pipelineTranslate.put("PARTINGTON", "Partington");
        pipelineTranslate.put("MILFORD HAVEN - SOUTH HOOK", "South Hook");
        pipelineTranslate.put("MILFORD HAVEN - DRAGON", "Dragon");
        pipelineTranslate.put("THEDDLETHORPE", "Theddlethorpe");
        pipelineTranslate.put("BARROW SOUTH", "Barrow South");
        pipelineTranslate.put("EASINGTON ROUGH", "Rough Storage");
    }


    public Terminal(String name) {
        terminalName = name;
    }

    public Terminal addPipeline(Pipeline pipe) {
        if (pipelineTranslate.containsKey(pipe.pipelineName)) {
            pipe.pipelineName = (String) pipelineTranslate.get(pipe.pipelineName);
        }
        pipelines.add(pipe);
        terminalTimestamp = pipelines.get(0).timestamp;
        terminalFlow = terminalFlow + pipe.flowValue;
        return this;
    }
}