package data;

import java.util.*;

/**
 * Created by Joe on 01/10/2016.
 */
public class TerminalHistory {
    public List<TerminalDataPoint> data = new ArrayList<>();

    public void addDatapoint(String rawTS, String rawFlow) {
        Date timestamp = ConvertTimestamp.timestampConverter2(rawTS);
        String flow = String.format(Locale.UK, "%.2f", Double.parseDouble(rawFlow));
        TerminalDataPoint dataPoint = new TerminalDataPoint(timestamp, flow);
        data.add(dataPoint);
        Collections.sort(data);
    }


}
