package data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joe on 30/09/2016.
 */
public class ConvertTimestamp {

    public static Date timestampConverter(String rawTS){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try {
            return df.parse(rawTS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date timestampConverter2(String rawTS) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return df.parse(rawTS);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
