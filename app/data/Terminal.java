package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Joe on 03/09/2016.
 */
@SuppressWarnings("WeakerAccess")
public class Terminal implements Comparable<Terminal> {
    public String terminalName;
    public Date terminalTimestamp;
    public double terminalFlow = 0.0;
    public final ArrayList<Pipeline> pipelines = new ArrayList<>();
    private static final HashMap<String, String> PIPELINE_TRANSLATE = new HashMap<>();
    static {
        PIPELINE_TRANSLATE.put("BACTON BBL", "Bacton BBL");
        PIPELINE_TRANSLATE.put("BACTON IC", "Interconnector");
        PIPELINE_TRANSLATE.put("BACTON PERENCO", "Bacton Perenco");
        PIPELINE_TRANSLATE.put("BACTON SEAL", "Bacton SEAL");
        PIPELINE_TRANSLATE.put("BACTON SHELL", "Bacton Shell");
        PIPELINE_TRANSLATE.put("ST FERGUS MOBIL", "St Fergus Mobil");
        PIPELINE_TRANSLATE.put("ST FERGUS NSMP", "St Fergus NSMP");
        PIPELINE_TRANSLATE.put("ST FERGUS SHELL", "St Fergus Shell");
        PIPELINE_TRANSLATE.put("TEESSIDE PX", "Teesside PX");
        PIPELINE_TRANSLATE.put("TEESSIDE BP", "Teesside CATS");
        PIPELINE_TRANSLATE.put("ALDBROUGH", "Aldbrough");
        PIPELINE_TRANSLATE.put("HILLTOP", "Hilltop");
        PIPELINE_TRANSLATE.put("HOLE HOUSE FARM", "Hole House Farm");
        PIPELINE_TRANSLATE.put("HOLFORD", "Holford");
        PIPELINE_TRANSLATE.put("HORNSEA", "Hornsea");
        PIPELINE_TRANSLATE.put("STUBLACH", "Stublach");
        PIPELINE_TRANSLATE.put("GRAIN NTS 1", "Grain NTS 1");
        PIPELINE_TRANSLATE.put("GRAIN NTS 2", "Grain NTS 2");
        PIPELINE_TRANSLATE.put("EASINGTON DIMLINGTON", "Easington Dimlington");
        PIPELINE_TRANSLATE.put("EASINGTON LANGELED", "Easington Langeled");
        PIPELINE_TRANSLATE.put("AVONMOUTH", "Avonmouth");
        PIPELINE_TRANSLATE.put("GLENMAVIS", "Glenmavis");
        PIPELINE_TRANSLATE.put("DYNEVOR ARMS", "Dynevor Arms");
        PIPELINE_TRANSLATE.put("PARTINGTON", "Partington");
        PIPELINE_TRANSLATE.put("MILFORD HAVEN - SOUTH HOOK", "South Hook");
        PIPELINE_TRANSLATE.put("MILFORD HAVEN - DRAGON", "Dragon");
        PIPELINE_TRANSLATE.put("THEDDLETHORPE", "Theddlethorpe");
        PIPELINE_TRANSLATE.put("BARROW SOUTH", "Barrow South");
        PIPELINE_TRANSLATE.put("EASINGTON ROUGH", "Rough Storage");
    }

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
        if (PIPELINE_TRANSLATE.containsKey(pipe.pipelineName)) {
            pipe.pipelineName = PIPELINE_TRANSLATE.get(pipe.pipelineName);
        }
        pipelines.add(pipe);
        terminalTimestamp = pipelines.get(0).timestamp;
        terminalFlow = terminalFlow + pipe.flowValue;
    }
}