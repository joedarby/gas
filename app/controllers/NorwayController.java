package controllers;

import data.HTMLGetter;
import data.NorwayDataSet;
import org.jsoup.nodes.Document;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSCookie;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class NorwayController extends Controller{

    @Inject
    private WSClient ws;

    public Result index() {

        Document doc;
        try {
            List<WSCookie> cookies = HTMLGetter.getCookies(ws, "http://flow.gassco.no/");
            WSCookie cookie = cookies.get(0);
            doc = HTMLGetter.getNorwayHTMLDocument(ws, "http://flow.gassco.no/acceptDisclaimer", cookie);
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

        NorwayDataSet data = new NorwayDataSet(doc);

        return ok(Json.toJson(data));


    }

}
