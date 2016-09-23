package controllers;

import data.TerminalMap;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.util.parsing.json.JSONObject;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Joe on 23/09/2016.
 */
public class DatabaseOutputTerminal extends Controller {

    @Inject
    Database database;

    public Result index(String name) {
        String terminal = name;
        HashMap mapout = new HashMap();
        Connection connection = database.getConnection();

        try {
            String selectStatement = "SELECT timestamp, \""+ terminal + "\" FROM terminals";
            CallableStatement statement = connection.prepareCall(selectStatement);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                mapout.put(result.getString(1), String.format(Locale.UK, "%.2f", Double.parseDouble(result.getString(2))));
            }
            connection.close();
            return ok(Json.toJson(mapout));

        } catch (SQLException e) {
            return internalServerError(e.toString());
        }


    }


}
