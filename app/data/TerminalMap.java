package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joe on 03/09/2016.
 */
public class TerminalMap {
    public HashMap terminalMapping = new HashMap();
    public Set<String> terminalGroupNames = new HashSet<>();

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
        terminalMapping.put("BARROW SOUTH", "BARROW");
        terminalMapping.put("THEDDLETHORPE", "THEDDLETHORPE");

        for ( Object value : terminalMapping.values() ) {
            terminalGroupNames.add((String) value);
        }
        terminalGroupNames.add("OTHER");
    }

    //Given terminal name, return terminal group name
    public static String getTerminalGroup(String tName) {
        HashMap map = new TerminalMap().terminalMapping;
        String groupToAddTo;
        if (map.containsKey(tName)) {
            groupToAddTo = (String) map.get(tName);
        } else {
            groupToAddTo = "OTHER";
        }
        return groupToAddTo;
    }

    // Generate an empty HashMap of the terminal groups
    public static HashMap<String, TerminalGroup> initiateTerminalGroupList() {
        Set<String> terminalGroupNames = new TerminalMap().terminalGroupNames;
        HashMap<String, TerminalGroup> terminalGroupList = new HashMap<>();
        for (String terminalGroupName : terminalGroupNames) {
            TerminalGroup terminalGroup = new TerminalGroup(terminalGroupName);
            terminalGroupList.put(terminalGroupName, terminalGroup);
        }
        return terminalGroupList;
    }
}
