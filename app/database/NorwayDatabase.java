package database;

import data.*;
import play.Logger;
import play.db.Database;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Joe on 28/09/2016.
 */
public class NorwayDatabase {
    private final Connection connection;
    private HashMap<String, Double> flows = new HashMap<>();
    private Timestamp sqlTimestamp;
    public static String[] LOCATIONS = {"Langeled", "Vesterled", "SEGAL", "Zeebrugge", "Dunkerque", "Emden", "Dornum"};

    public NorwayDatabase(Database database) {
        connection = database.getConnection();
    }

    public void checkAndAddToDatabase(NorwayDataSet data) throws Exception {
        flows = data.norwayFlows;
        Date timestamp = data.timestamp;
        sqlTimestamp = new Timestamp(timestamp.getTime());

        connection.prepareCall(getCreateStatement()).execute();
        if (!(flows.equals(dbGetLastRow()))) {
            dbInsert();
        }


        connection.close();
    }

    private String getCreateStatement() {
        String createStatement = "CREATE TABLE IF NOT EXISTS norway (timestamp TIMESTAMP";

        for (String location : LOCATIONS) {
            createStatement += ", \"" + location + "\" DECIMAL";
        }
        createStatement += ")";
        return createStatement;
    }

    private String getInsertStatement() {
        String insertStatement = "INSERT INTO norway (timestamp";
        for (String location : LOCATIONS) {
            insertStatement += ", \"" + location + "\"";
        }

        insertStatement += ") VALUES (?";
        for (int i = 0; i < LOCATIONS.length; i++) {
            insertStatement += ", ?";
        }

        insertStatement += ")";
        return insertStatement;
    }

    private HashMap<String, Double> dbGetLastRow() throws Exception {
        HashMap<String, Double> lastRow = new HashMap<>();

        CallableStatement getLastEntry = connection.prepareCall("SELECT MAX(timestamp) FROM norway");
        ResultSet lastEntryResult = getLastEntry.executeQuery();


        if((lastEntryResult.next())) {

            if (lastEntryResult.getTimestamp(1) != null) {
                String selectStatement = "SELECT * FROM norway WHERE timestamp = " + "\'" + lastEntryResult.getTimestamp(1) + "\'";
                CallableStatement getResult = connection.prepareCall(selectStatement);
                ResultSet result = getResult.executeQuery();

                while (result.next()) {
                    int i = 2;
                    for (String location : LOCATIONS) {
                        lastRow.put(location, result.getDouble(i));
                        i++;
                    }
                }

            }

        } else {
            System.out.println("table empty");
        }

        return lastRow;
    }

    private void dbInsert() throws SQLException {
        String insertStatement = getInsertStatement();

        CallableStatement insert = connection.prepareCall(insertStatement);
        insert.setTimestamp(1, sqlTimestamp);
        for (String flow : flows.keySet()) {
            int i = 2;
            for (String location : LOCATIONS) {
                if (location.equals(flow)) {
                    insert.setDouble(i, flows.get(flow));
                } else {
                    i += 1;
                }
            }
        }
        insert.execute();
        Logger.info("New Norway data inserted at " + new Date().toString());
    }


}


















