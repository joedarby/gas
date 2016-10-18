package database;

import data.Pipeline;
import data.Terminal;
import data.TerminalHelper;
import data.TerminalMap;
import play.db.Database;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 28/09/2016.
 */
public class UKDatabase {
    private final Connection connection;
    private List<Terminal> terminals;
    private Timestamp sqlTimestamp;

    public UKDatabase(Database database) {
        connection = database.getConnection();
    }

    public void checkAndAddToDatabase(List<Terminal> terms) {
        terminals = terms;
        Date timestamp = terms.get(0).terminalTimestamp;
        sqlTimestamp = new Timestamp(timestamp.getTime());

        try {
            connection.prepareCall(getCreateStatement()).execute();
            if (!dbCheckDuplicate()) {
                dbInsert();
            }
            connection.close();

        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCreateStatement() {
        String createStatement = "CREATE TABLE IF NOT EXISTS terminals (timestamp TIMESTAMP";
        for (String name : TerminalHelper.PIPELINE_TRANSLATE.values()) {
            createStatement += ", \"" + name + "\" DECIMAL";
        }
        for (String name : TerminalMap.TERMINAL_NAMES) {
            createStatement += ", \"" + name + "\" DECIMAL";
        }
        createStatement += ")";
        return createStatement;
    }

    private String getInsertStatement() {
        String insertStatement = "INSERT INTO terminals (timestamp";
        for (String name : TerminalHelper.PIPELINE_TRANSLATE.values()) {
            insertStatement += ", \"" + name + "\"";
        }
        for (String name : TerminalMap.TERMINAL_NAMES) {
            insertStatement += ", \"" + name + "\"";
        }
        insertStatement += ") VALUES (?";
        for (int i = 0; i < TerminalHelper.PIPELINE_TRANSLATE.values().size(); i++) {
            insertStatement += ", ?";
        }
        for (int i = 0; i < TerminalMap.TERMINAL_NAMES.size(); i++) {
            insertStatement += ", ?";
        }
        insertStatement += ")";
        return insertStatement;
    }

    private boolean dbCheckDuplicate() throws SQLException, ParseException {
        String selectStatement = "SELECT 1 FROM terminals WHERE timestamp = \'" + sqlTimestamp +"\'";
        //Next two lines allow the query to be "scrollable" so that the first() method will work.
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet result = stmt.executeQuery(selectStatement);
        return result.first();
    }

    private void dbInsert() throws SQLException {
        String insertStatement = getInsertStatement();

        CallableStatement insert = connection.prepareCall(insertStatement);
        insert.setTimestamp(1, sqlTimestamp);
        for (Terminal terminal : terminals) {
            for (Pipeline pipeline : terminal.pipelines) {
                int i = 2;
                for (String pipelineName : TerminalHelper.PIPELINE_TRANSLATE.values()) {
                    if (pipeline.pipelineName.equals(pipelineName)) {
                        insert.setDouble(i, pipeline.flowValue);
                    } else {
                        i += 1;
                    }
                }

            }
        }
        for (Terminal terminal : terminals) {
            int i = 31;
            for (String terminalName : TerminalMap.TERMINAL_NAMES) {
                if (terminal.terminalName.equals(terminalName)) {
                    insert.setDouble(i, terminal.terminalFlow);
                } else {
                    i += 1;
                }
            }
        }
        insert.execute();
    }


}


















