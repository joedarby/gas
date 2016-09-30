package controllers;

import data.ConvertTimestamp;
import data.TerminalDataPoint;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Joe on 23/09/2016.
 */
public class DatabaseOutputTerminal extends Controller {

    @Inject
    Database database;

    public Result index(String terminalName) {
        List<TerminalDataPoint> dataList = new ArrayList<>();
        Connection connection = database.getConnection();

        try {
            String selectStatement = "SELECT timestamp, \""+ terminalName + "\" FROM terminals";
            CallableStatement statement = connection.prepareCall(selectStatement);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                String rawTS = result.getString(1);
                System.out.println(rawTS);
                Date timestamp = ConvertTimestamp.timestampConverter2(rawTS);
                String flow = String.format(Locale.UK, "%.2f", Double.parseDouble(result.getString(2)));
                TerminalDataPoint dataPoint = new TerminalDataPoint(timestamp, flow);
                dataList.add(dataPoint);
            }

            connection.close();

            Collections.sort(dataList);
            return ok(Json.toJson(dataList));

        } catch (SQLException e) {
            return internalServerError(e.toString());
        }


    }


}
