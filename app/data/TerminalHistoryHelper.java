package data;

import play.db.Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Joe on 05/10/2016.
 */
public class TerminalHistoryHelper {
    public static TerminalHistory getTerminalHistory(Database database, String terminalName) {
        TerminalHistory history = new TerminalHistory();
        Connection connection = database.getConnection();

        String selectStatement = "SELECT timestamp, \""+ terminalName + "\" FROM terminals";

        try{
            CallableStatement statement = connection.prepareCall(selectStatement);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                String rawTS = result.getString(1);
                String rawFlow = result.getString(2);
                history.addDatapoint(rawTS, rawFlow);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return history;


    }

    //Takes a terminal history dataset and returns a chart data object, a wrapper of a hashmap containing time indexes (minutes since the chart start point) and flow values.
    public static ChartData getChartData(TerminalHistory history) {
        ChartData chartData = new ChartData();
        long startTime = setChartStart();
        for (TerminalDataPoint dp : history.data) {
            float flow = Float.parseFloat(dp.flowRate);
            long dpMillis = dp.timestamp.getTime();
            if (dpMillis > startTime) {
                float timeIndex = (dpMillis - startTime) / 1000f / 60f;
                chartData.dataList.put(timeIndex, flow);
            }
        }
        return chartData;
    }

    //Works out the milli time where the terminal chart begins
    public static long setChartStart() {
        //Get calendar for right now
        Calendar rightNow = Calendar.getInstance();
        //Instantiate another calendar to set as chart begin;
        Calendar chartStart = Calendar.getInstance();
        //If current time is between midnight and 05:00, set chart begin as 05:00 on current calendar day minus 3.
        if (rightNow.get(Calendar.HOUR_OF_DAY) < 5) {
            chartStart.add(Calendar.DAY_OF_MONTH, -3);
        } else {
            ////If current time is after 05:00, set chart begin as 05:00 on current calendar day minus 2.
            chartStart.add(Calendar.DAY_OF_MONTH, -2);
        }
        //Set hours, minutes, seconds and milliseconds to exactly 05:00
        chartStart.set(Calendar.HOUR_OF_DAY, 5);
        chartStart.set(Calendar.MINUTE, 0);
        chartStart.set(Calendar.SECOND, 0);
        chartStart.set(Calendar.MILLISECOND, 0);

        return chartStart.getTimeInMillis();
    }


}
