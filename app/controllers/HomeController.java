package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import scala.util.parsing.json.JSONArray;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    @Inject WSClient ws;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {

        CompletionStage<WSResponse> futureResponse = ws.url("http://energywatch.natgrid.co.uk/EDP-PublicUI/Public/InstantaneousFlowsIntoNTS.aspx?CalledFrom=nguk")
                .setContentType("application/x-www-form-urlencoded")
                .post("__EVENTTARGET=a1&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwULLTEyMjEyNjg2NzlkGAIFDmd2SW5zdGFudFRhYmxlDzwrAAoBCAIBZAUGdGFibGUzDzwrAAoBCAIBZJ7q2q75hRISuNNRLVb%2BQrFJ%2Bj5M&__VIEWSTATEGENERATOR=27867E27&__EVENTVALIDATION=%2FwEWAwL%2Bn5W7DgK%2F7%2BbtDALDmNLsDhsc68eKkomPaNlEXMwXYS14SX%2Bw");

        try {
                WSResponse response = futureResponse.toCompletableFuture().get();
                String body = response.getBody();
                String[] lines = body.split("\n");

                // Do your processing here


                JsonNode json = Json.newObject()
                        .putArray("terminal").add(lines[1])
                        .add(lines[2]);

                return ok(json);
        } catch (Exception e ) {
            return internalServerError(e.toString());
        }
    }

}
