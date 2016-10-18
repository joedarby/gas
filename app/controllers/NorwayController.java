package controllers;

import data.HTMLGetter;
import data.NorwayDataSet;
import database.NorwayDatabase;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.nodes.Document;
import play.Logger;
import play.db.Database;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSCookie;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class NorwayController extends Controller {

    @Inject
    private WSClient ws;

    @Inject
    Database database;

    public Result index() {

        try {
            return ok(Json.toJson(DataRefresh(ws, database)));
        } catch (Exception e) {
            return internalServerError(ExceptionUtils.getStackTrace(e));
        }


    }

    public static NorwayDataSet DataRefresh(WSClient ws, Database db) throws Exception {
        Document doc;

        List<WSCookie> cookies = HTMLGetter.getCookies(ws, "http://flow.gassco.no/");
        WSCookie cookie = cookies.get(0);
        doc = HTMLGetter.getNorwayHTMLDocument(ws, "http://flow.gassco.no/acceptDisclaimer", cookie);

        NorwayDataSet norwayDataSet = new NorwayDataSet(doc);
        new NorwayDatabase(db).checkAndAddToDatabase(norwayDataSet);

        return norwayDataSet;

    }


}



