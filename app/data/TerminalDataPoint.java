package data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by Joe on 30/09/2016.
 */
public class TerminalDataPoint implements Comparable<TerminalDataPoint> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    public Date timestamp;
    public  String flowRate;

    public TerminalDataPoint(Date time, String flow) {
        timestamp = time;
        flowRate = flow;
    }

    @Override
    public int compareTo(TerminalDataPoint another) {
        if (this.timestamp.before(another.timestamp)) {
            return 1;
        } else {
            return -1;
        }
    }
}
