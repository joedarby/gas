package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe on 03/09/2016.
 */
public class TerminalMap {
    private final HashMap<String, String> terminalMapping = new HashMap<>();
    private final Set<String> terminalNames = new HashSet<>();

    private TerminalMap() {

        terminalMapping.put("BACTON BBL", "Bacton IP");
        terminalMapping.put("BACTON IC", "Bacton IP");
        terminalMapping.put("BACTON PERENCO", "Bacton UKCS");
        terminalMapping.put("BACTON SEAL", "Bacton UKCS");
        terminalMapping.put("BACTON SHELL", "Bacton UKCS");
        terminalMapping.put("ST FERGUS MOBIL", "St Fergus");
        terminalMapping.put("ST FERGUS NSMP", "St Fergus");
        terminalMapping.put("ST FERGUS SHELL", "St Fergus");
        terminalMapping.put("TEESSIDE PX", "Teesside");
        terminalMapping.put("TEESSIDE BP", "Teesside");
        terminalMapping.put("ALDBROUGH", "Medium Range Storage");
        terminalMapping.put("HILLTOP", "Medium Range Storage");
        terminalMapping.put("HOLE HOUSE FARM", "Medium Range Storage");
        terminalMapping.put("HOLFORD", "Medium Range Storage");
        terminalMapping.put("HORNSEA", "Medium Range Storage");
        terminalMapping.put("STUBLACH", "Medium Range Storage");
        terminalMapping.put("GRAIN NTS 1", "Isle of Grain");
        terminalMapping.put("GRAIN NTS 2", "Isle of Grain");
        terminalMapping.put("EASINGTON DIMLINGTON", "Easington");
        terminalMapping.put("EASINGTON LANGELED", "Easington");
        terminalMapping.put("AVONMOUTH", "LNG Storage");
        terminalMapping.put("GLENMAVIS", "LNG Storage");
        terminalMapping.put("DYNEVOR ARMS", "LNG Storage");
        terminalMapping.put("PARTINGTON", "LNG Storage");
        terminalMapping.put("MILFORD HAVEN - SOUTH HOOK", "Milford Haven");
        terminalMapping.put("MILFORD HAVEN - DRAGON", "Milford Haven");
        terminalMapping.put("THEDDLETHORPE", "Theddlethorpe");
        terminalMapping.put("BARROW SOUTH", "Barrow");
        terminalMapping.put("EASINGTON ROUGH", "Rough Storage");

        terminalNames.addAll(terminalMapping.values());
    }

    //Given pipeline name, return terminal name
    public static String getTerminal(String tName) {
        HashMap map = new TerminalMap().terminalMapping;
        String terminalToAddTo;
        if (map.containsKey(tName)) {
            terminalToAddTo = (String) map.get(tName);
        } else {
            terminalToAddTo = null;
        }
        return terminalToAddTo;
    }

    // Generate an empty HashMap of the terminals
    public static HashMap<String, Terminal> initiateTerminalList() {
        Set<String> terminalNames = new TerminalMap().terminalNames;
        HashMap<String, Terminal> terminalList = new HashMap<>();
        for (String terminalName : terminalNames) {
            Terminal terminal = new Terminal(terminalName);
            terminalList.put(terminalName, terminal);
        }
        return terminalList;
    }
}
