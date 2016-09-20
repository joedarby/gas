package controllers;

import data.LinepackDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
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
             doc = Jsoup.parse(getPage().getBody());
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

        LinepackDataSet data = new LinepackDataSet(doc);

        return ok(Json.toJson(data));


    }

    private WSResponse getPage() throws Exception {

        CompletionStage<WSResponse> response = ws.url("http://marketinformation.natgrid.co.uk/gas/frmPrevalingView.aspx")
                .get();

        return response.toCompletableFuture().get();
    }

}
