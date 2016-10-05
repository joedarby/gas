package controllers;

import data.TerminalHistoryHelper;
import data.TerminalHistory;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by Joe on 23/09/2016.
 */
public class TerminalHistoryController extends Controller {

    @Inject
    Database database;

    public Result index(String terminalName) {
        TerminalHistory history = TerminalHistoryHelper.getTerminalHistory(database, terminalName);

        return ok(Json.toJson(history));
    }


}
