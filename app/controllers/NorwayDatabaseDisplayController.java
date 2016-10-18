package controllers;

import data.TerminalHelper;
import data.TerminalMap;
import database.NorwayDatabase;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by Joe on 23/09/2016.
 */
public class NorwayDatabaseDisplayController extends Controller {

    @Inject
    Database database;

    public Result index() {
        Connection connection = database.getConnection();
        String output = "Norway\ntimestamp";
        for (String location : NorwayDatabase.LOCATIONS) {
            output += " | " + location;
        }
        output += "\n";

        try {
            CallableStatement statement = connection.prepareCall("SELECT * FROM norway");
            ResultSet result = statement.executeQuery();

            //For each row in the result, add the timestamp at position 1, and all of the flow values in the row, to the output string
            while(result.next()) {
                output += result.getString(1);
                for (int i = 2; i < (NorwayDatabase.LOCATIONS.length+2); i++) {
                    String item = String.format(Locale.UK, "%.2f", Double.parseDouble(result.getString(i)));
                    output += " | " + item;
                }
                output += "\n";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ok(output);

    }
}
