package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe on 03/09/2016.
 */
public class TerminalMap {
    public HashMap terminalMapping = new HashMap();
    public Set<String> terminalNames = new HashSet<>();

    public TerminalMap() {

        terminalMapping.put("BACTON BBL", "BACTON IP");
        terminalMapping.put("BACTON IC", "BACTON IP");
        terminalMapping.put("BACTON PERENCO", "BACTON UKCS");
        terminalMapping.put("BACTON SEAL", "BACTON UKCS");
        terminalMapping.put("BACTON SHELL", "BACTON UKCS");
        terminalMapping.put("ST FERGUS MOBIL", "ST FERGUS");
        terminalMapping.put("ST FERGUS NSMP", "ST FERGUS");
        terminalMapping.put("ST FERGUS SHELL", "ST FERGUS");
        terminalMapping.put("TEESSIDE PX", "TEESSIDE");
        terminalMapping.put("TEESSIDE BP", "TEESSIDE");
        terminalMapping.put("ALDBROUGH", "MEDIUM RANGE STORAGE");
        terminalMapping.put("HILLTOP", "MEDIUM RANGE STORAGE");
        terminalMapping.put("HOLE HOUSE FARM", "MEDIUM RANGE STORAGE");
        terminalMapping.put("HOLFORD", "MEDIUM RANGE STORAGE");
        terminalMapping.put("HORNSEA", "MEDIUM RANGE STORAGE");
        terminalMapping.put("STUBLACH", "MEDIUM RANGE STORAGE");
        terminalMapping.put("GRAIN NTS 1", "ISLE OF GRAIN");
        terminalMapping.put("GRAIN NTS 2", "ISLE OF GRAIN");
        terminalMapping.put("EASINGTON DIMLINGTON", "EASINGTON");
        terminalMapping.put("EASINGTON LANGELED", "EASINGTON");
        terminalMapping.put("AVONMOUTH", "LNG STORAGE");
        terminalMapping.put("GLENMAVIS", "LNG STORAGE");
        terminalMapping.put("DYNEVOR ARMS", "LNG STORAGE");
        terminalMapping.put("PARTINGTON", "LNG STORAGE");
        terminalMapping.put("MILFORD HAVEN - SOUTH HOOK", "MILFORD HAVEN");
        terminalMapping.put("MILFORD HAVEN - DRAGON", "MILFORD HAVEN");

        for ( Object value : terminalMapping.values() ) {
            terminalNames.add((String) value);
        }
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
