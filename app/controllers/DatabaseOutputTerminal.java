package controllers;

import data.TerminalHistory;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Joe on 23/09/2016.
 */
public class DatabaseOutputTerminal extends Controller {

    @Inject
    Database database;

    public Result index(String terminalName) {
        TerminalHistory history = new TerminalHistory();
        Connection connection = database.getConnection();

        try {
            String selectStatement = "SELECT timestamp, \""+ terminalName + "\" FROM terminals";
            CallableStatement statement = connection.prepareCall(selectStatement);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                String rawTS = result.getString(1);
                String rawFlow = result.getString(2);
                history.addDatapoint(rawTS, rawFlow);
                            }

            connection.close();

            return ok(Json.toJson(history));

        } catch (SQLException e) {
            return internalServerError(e.toString());
        }


    }


}
