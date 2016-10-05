package controllers;

import data.ChartData;
import data.TerminalHistoryHelper;
import data.TerminalHistory;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by Joe on 05/10/2016.
 */
public class ChartDataController extends Controller {

    @Inject
    Database database;

    public Result index(String terminalName) {
        TerminalHistory history = TerminalHistoryHelper.getTerminalHistory(database, terminalName);
        ChartData chartData = TerminalHistoryHelper.getChartData(history);

        return ok(Json.toJson(chartData));

    }















}
