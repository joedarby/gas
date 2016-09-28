package controllers;

import data.Terminal;
import data.TerminalMap;
import play.db.Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 28/09/2016.
 */
public class DatabaseBuilder {
    Connection connection;
    List<Terminal> terminals;

    public DatabaseBuilder(Database database, List<Terminal> terms) {
        connection = database.getConnection();
        terminals = terms;

        try {
            String createStatement = "CREATE TABLE IF NOT EXISTS terminals (timestamp TIMESTAMP";
            for (String name : TerminalMap.terminalNames) {
                createStatement += ", \"" + name + "\" DECIMAL";
            }
            createStatement += ")";
            connection.prepareCall(createStatement).execute();
            dbCheckDuplicate();
            if (!dbCheckDuplicate()) {
                dbInsert();
            } else {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean dbCheckDuplicate() {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(terminals.get(0).terminalTimestamp);
            Timestamp timestamp = new Timestamp(date.getTime());
            String selectStatement = "SELECT count(*) FROM terminals WHERE timestamp = " + timestamp;
            return connection.prepareCall(selectStatement).execute();
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void dbInsert() {
        String insertStatement = "INSERT INTO terminals (timestamp";
        for (String name : TerminalMap.terminalNames) {
            insertStatement += ", \"" + name + "\"";
        }
        insertStatement += ") VALUES (?";
        for (int i = 0; i < TerminalMap.terminalNames.size(); i++) {
            insertStatement += ", ?";
        }
        insertStatement += ")";

        try {
            CallableStatement insert = connection.prepareCall(insertStatement);
            for (Terminal terminal : terminals) {
                int i = 2;
                for (String terminalName : TerminalMap.terminalNames) {
                    if (terminal.terminalName.equals(terminalName)) {
                        insert.setDouble(i, terminal.terminalFlow);
                    } else {
                        i += 1;
                    }
                }
            }
            insert.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}


















