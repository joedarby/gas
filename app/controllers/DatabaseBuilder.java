package controllers;

import data.Terminal;
import data.TerminalMap;
import play.db.Database;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 28/09/2016.
 */
public class DatabaseBuilder {
    private final Connection connection;
    private final List<Terminal> terminals;
    private Timestamp timestamp;

    public DatabaseBuilder(Database database, List<Terminal> terms) {
        connection = database.getConnection();
        terminals = terms;
    }

    public void dbInsertTerminal() {
        try {
            connection.prepareCall(getCreateStatement()).execute();
            if (!dbCheckDuplicate()) {
                dbInsert();
            } else {
                connection.close();
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCreateStatement() {
        String createStatement = "CREATE TABLE IF NOT EXISTS terminals (timestamp TIMESTAMP";
        for (String name : TerminalMap.terminalNames) {
            createStatement += ", \"" + name + "\" DECIMAL";
        }
        createStatement += ")";
        return createStatement;
    }

    private String getInsertStatement() {
        String insertStatement = "INSERT INTO terminals (timestamp";
        for (String name : TerminalMap.terminalNames) {
            insertStatement += ", \"" + name + "\"";
        }
        insertStatement += ") VALUES (?";
        for (int i = 0; i < TerminalMap.terminalNames.size(); i++) {
            insertStatement += ", ?";
        }
        insertStatement += ")";
        return insertStatement;
    }

    private boolean dbCheckDuplicate() throws SQLException, ParseException {
        Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(terminals.get(0).terminalTimestamp);
        timestamp = new Timestamp(date.getTime());
        String timestampText = timestamp.toString().substring(0,21);
        System.out.println(timestampText);
        String selectStatement = "SELECT 1 FROM terminals WHERE timestamp = \'" + timestamp +"\'";
        return connection.prepareCall(selectStatement).executeQuery().first();
    }

    private void dbInsert() throws SQLException {
        String insertStatement = getInsertStatement();

        CallableStatement insert = connection.prepareCall(insertStatement);
        insert.setTimestamp(1, timestamp);
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
    }


}


















