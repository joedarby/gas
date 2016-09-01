package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.Terminal;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;


public class HomeController extends Controller {

    // The WSClient is injected automatically by the framework
    // The WSClient is used to do HTTP requests to other services (eg, to grab our CSV)
    @Inject WSClient ws;

    public Result index() {

        // We need to request the CSV file from the national grid website
        //
        // This requires us to do a POST request to their server, passing some hard coded form parameters in the body
        // of the request
        CompletionStage<WSResponse> futureResponse = ws.url("http://energywatch.natgrid.co.uk/EDP-PublicUI/Public/InstantaneousFlowsIntoNTS.aspx?CalledFrom=nguk")
                .setContentType("application/x-www-form-urlencoded")
                .post("__EVENTTARGET=a1&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwULLTEyMjEyNjg2NzlkGAIFDmd2SW5zdGFudFRhYmxlDzwrAAoBCAIBZAUGdGFibGUzDzwrAAoBCAIBZJ7q2q75hRISuNNRLVb%2BQrFJ%2Bj5M&__VIEWSTATEGENERATOR=27867E27&__EVENTVALIDATION=%2FwEWAwL%2Bn5W7DgK%2F7%2BbtDALDmNLsDhsc68eKkomPaNlEXMwXYS14SX%2Bw");

        // Set the response to null initially
        WSResponse response = null;

        // Attempt to request the CSV.
        try {
                response = futureResponse.toCompletableFuture().get();
        } catch (Exception e ) { // If there is an error of any kind, catch it and show an error page
            return internalServerError(e.toString());
        }

        // Get the CSV body
        String body = response.getBody();

        // Split the CSV into an array of lines, using the split command, passing the 'newline' (\n) character as the character to split on
        String[] lines = body.split("\n");

        // Create a list of Terminals to return in our response
        ArrayList<Terminal> terminalList = new ArrayList<Terminal>();

        // Do your processing here
        for(String line: lines) {
            // look at each line and do some work

            // Splitting on the ',' character here can pull out various fields of the CSV row
            String terminalName = line.split(",")[0];
            terminalList.add(new Terminal(terminalName));
        }

        // We convert the list of terminals automagically into JSON
        return ok(Json.toJson(terminalList));
    }

}
