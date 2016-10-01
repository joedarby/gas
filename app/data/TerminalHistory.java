package data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joe on 01/10/2016.
 */
public class TerminalHistory {
    public static List<TerminalDataPoint> data;

    public void addDatapoint(String rawTS, String rawFlow) {
        Date timestamp = ConvertTimestamp.timestampConverter2(rawTS);
        String flow = String.format(Locale.UK, "%.2f", Double.parseDouble(rawFlow));
        TerminalDataPoint dataPoint = new TerminalDataPoint(timestamp, flow);
        data.add(dataPoint);
        Collections.sort(data);
    }


}
