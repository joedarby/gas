package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import data.HTMLGetter;
import data.LinepackDataSet;
import org.jsoup.nodes.Document;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Created by Joe on 20/09/2016.
 */
public class LinepackController extends Controller{

    @Inject
    private WSClient ws;

    public Result index() {

        Document doc;
        try {
             doc = HTMLGetter.getHTMLDocument(ws,"http://mip-prod-web.azurewebsites.net/PrevailingView");
        } catch (Exception e) {
            return internalServerError(e.toString());
        }
        JsonNode json = getJson(doc);

        LinepackDataSet data = new LinepackDataSet(json);

        return ok(Json.toJson(data));


    }

    private JsonNode getJson(Document doc) {
        String rawDoc = doc.toString()
                .split("var dynamicData=")[1]
                .split(";")[0];

        return Json.parse(rawDoc);
    }
}
