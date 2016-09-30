package controllers;

import data.TerminalMap;
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
public class DatabaseDisplayController extends Controller {

    @Inject
    Database database;

    public Result index() {
        Connection connection = database.getConnection();
        String output = "timestamp";
        for (String term : TerminalMap.terminalNames) {
            output += " " + term;
        }
        output += "\n";

        try {
            CallableStatement statement = connection.prepareCall("SELECT * FROM terminals");
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                output += result.getString(1);
                for (int i = 2; i < (TerminalMap.terminalNames.size()+2); i++) {
                    String item = String.format(Locale.UK, "%.2f", Double.parseDouble(result.getString(i)));
                    output += " " + item;
                }
                output += "\n";
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ok(output);

    }
}
