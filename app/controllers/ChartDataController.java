package controllers;

import data.ChartData;
import data.TerminalHistoryHelper;
import data.TerminalHistory;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by Joe on 05/10/2016.
 */
public class ChartDataController extends Controller {

    @Inject
    Database database;

    public Result index(String terminalNames) {
        String[] splitNames = terminalNames.split(",");
        ArrayList<TerminalHistory> historyObjects = new ArrayList<>();

        for (String name : splitNames) {
            TerminalHistory history = TerminalHistoryHelper.getTerminalHistory(database, name);
            historyObjects.add(history);
        }

        ChartData chartData = TerminalHistoryHelper.getChartData(historyObjects);

        return ok(Json.toJson(chartData));

    }















}
