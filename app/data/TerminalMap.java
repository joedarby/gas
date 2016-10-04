package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe on 03/09/2016.
 */
public class TerminalMap {
    public static final HashMap<String, String> TERMINAL_MAPPING = new HashMap<>();
    public static Set<String> TERMINAL_NAMES = new HashSet<>();
    static {
        TERMINAL_MAPPING.put("BACTON BBL", "Bacton IP");
        TERMINAL_MAPPING.put("BACTON IC", "Bacton IP");
        TERMINAL_MAPPING.put("BACTON PERENCO", "Bacton UKCS");
        TERMINAL_MAPPING.put("BACTON SEAL", "Bacton UKCS");
        TERMINAL_MAPPING.put("BACTON SHELL", "Bacton UKCS");
        TERMINAL_MAPPING.put("ST FERGUS MOBIL", "St Fergus");
        TERMINAL_MAPPING.put("ST FERGUS NSMP", "St Fergus");
        TERMINAL_MAPPING.put("ST FERGUS SHELL", "St Fergus");
        TERMINAL_MAPPING.put("TEESSIDE PX", "Teesside");
        TERMINAL_MAPPING.put("TEESSIDE BP", "Teesside");
        TERMINAL_MAPPING.put("ALDBROUGH", "Medium Range Storage");
        TERMINAL_MAPPING.put("HILLTOP", "Medium Range Storage");
        TERMINAL_MAPPING.put("HOLE HOUSE FARM", "Medium Range Storage");
        TERMINAL_MAPPING.put("HOLFORD", "Medium Range Storage");
        TERMINAL_MAPPING.put("HORNSEA", "Medium Range Storage");
        TERMINAL_MAPPING.put("STUBLACH", "Medium Range Storage");
        TERMINAL_MAPPING.put("GRAIN NTS 1", "Isle of Grain");
        TERMINAL_MAPPING.put("GRAIN NTS 2", "Isle of Grain");
        TERMINAL_MAPPING.put("EASINGTON DIMLINGTON", "Easington");
        TERMINAL_MAPPING.put("EASINGTON LANGELED", "Easington");
        TERMINAL_MAPPING.put("AVONMOUTH", "LNG Storage");
        TERMINAL_MAPPING.put("GLENMAVIS", "LNG Storage");
        TERMINAL_MAPPING.put("DYNEVOR ARMS", "LNG Storage");
        TERMINAL_MAPPING.put("PARTINGTON", "LNG Storage");
        TERMINAL_MAPPING.put("MILFORD HAVEN - SOUTH HOOK", "Milford Haven");
        TERMINAL_MAPPING.put("MILFORD HAVEN - DRAGON", "Milford Haven");
        TERMINAL_MAPPING.put("THEDDLETHORPE", "Theddlethorpe");
        TERMINAL_MAPPING.put("BARROW SOUTH", "Barrow");
        TERMINAL_MAPPING.put("EASINGTON ROUGH", "Rough Storage");

        TERMINAL_NAMES.addAll(TERMINAL_MAPPING.values());
    }



    //Given pipeline name, return terminal name
    public static String getTerminal(String tName) {
        String terminalToAddTo;
        if (TERMINAL_MAPPING.containsKey(tName)) {
            terminalToAddTo = TERMINAL_MAPPING.get(tName);
        } else {
            terminalToAddTo = null;
        }
        return terminalToAddTo;
    }

    // Generate an empty HashMap of the terminals
    public static HashMap<String, Terminal> initiateTerminalList() {
        HashMap<String, Terminal> terminalList = new HashMap<>();
        for (String terminalName : TERMINAL_NAMES) {
            Terminal terminal = new Terminal(terminalName);
            terminalList.put(terminalName, terminal);
        }
        return terminalList;
    }

}
