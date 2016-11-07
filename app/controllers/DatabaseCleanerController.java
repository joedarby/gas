package controllers;

import play.db.Database;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.sql.*;

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
        System.out.println(ukStatementString);

        String output = "UK rows deleted = ";

        try {
            CallableStatement statement = connection.prepareCall(ukStatementString);
            int rows = statement.executeUpdate();
            output += rows + "\n";


        } catch (SQLException e) {
            System.out.println(e.toString());
    }

        String norwayStatementString = "DELETE FROM norway WHERE timestamp < \'" + cutOffTimestamp + "\'";
        System.out.println(norwayStatementString);

        output += "Norway rows deleted = ";

        try {
            CallableStatement statement = connection.prepareCall(norwayStatementString);
            int rows = statement.executeUpdate();
            output += rows + "\n";

            connection.close();

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        
    return ok(output);

    }
}
