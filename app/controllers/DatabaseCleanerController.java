package controllers;

import play.Logger;
import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.*;
import java.util.Date;

/**
 * Created by Joe on 07/11/2016.
 */
public class DatabaseCleanerController extends Controller {

    @Inject
    Database database;

    public Result index() {
        Connection connection = database.getConnection();

        Timestamp cutOffTimestamp = new Timestamp(System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000));
        String ukStatementString = "DELETE FROM terminals WHERE timestamp < \'" + cutOffTimestamp + "\'";

        String output = "UK rows deleted = ";

        try {
            CallableStatement statement = connection.prepareCall(ukStatementString);
            int rows = statement.executeUpdate();
            output += rows + "\n";


        } catch (SQLException e) {
            Logger.error("UK database clean failed" + new Date().toString());

    }

        String norwayStatementString = "DELETE FROM norway WHERE timestamp < \'" + cutOffTimestamp + "\'";

        output += "Norway rows deleted = ";

        try {
            CallableStatement statement = connection.prepareCall(norwayStatementString);
            int rows = statement.executeUpdate();
            output += rows + "\n";

            connection.close();

        } catch (SQLException e) {
            Logger.error("Norway database clean failed" + new Date().toString());
        }

        Logger.info("Database cleaned at " + new Date().toString() + " up to " + cutOffTimestamp.toString());


    return ok(output);

    }
}
