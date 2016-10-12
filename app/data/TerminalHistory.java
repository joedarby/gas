package data;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Joe on 01/10/2016.
 */
public class TerminalHistory {
    public List<TerminalDataPoint> data = new ArrayList<>();
    public String pipelineName;

    public TerminalHistory(String name) {
        pipelineName = name;
    }

    public void addDatapoint(String rawTS, String rawFlow) {
        Date timestamp = ConvertTimestamp.rawTimestampToFormattedTimestampWithSeconds(rawTS);
        //String flow = String.format(Locale.UK, "%.2f", Double.parseDouble(rawFlow));
        BigDecimal flow = new BigDecimal(rawFlow).setScale(2, BigDecimal.ROUND_HALF_UP);
        TerminalDataPoint dataPoint = new TerminalDataPoint(timestamp, flow);
        data.add(dataPoint);
        Collections.sort(data);
    }


}
