package controllers;

import data.Terminal;
import data.TerminalGroup;
import data.TerminalMap;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;


public class HomeController extends Controller {

    // The WSClient is injected automatically by the framework
    // The WSClient is used to do HTTP requests to other services (eg, to grab our CSV)
    @Inject WSClient ws;

    public Result index() {

        // Call the getCSV method returning response
        WSResponse response;
        try {
            response = getCSV();
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

        // Get the CSV body
        String body = response.getBody();

        // Split the CSV into an array of lines, using the split command, passing the 'newline' (\n) character as the character to split on
        String[] lines = body.split("\n");

        String terminalName;
        Double flowValue;
        String timestamp;
        HashMap<String, TerminalGroup> terminalGroupList = TerminalMap.initiateTerminalGroupList();

      // Look at the name of the first terminal in the csv (ignore line 0 which is a header)
        String prevTerminalName = lines[1].split(",")[0];

        // Look through each line in the csv. When the terminal name changes, add the last line for the current terminal (most recent data) to the terminal list
        for (int i = 1; i < lines.length; i++) {
            terminalName = lines[i].split(",")[0];
            if (!Objects.equals(prevTerminalName, terminalName)) {
                String[] splitLine = lines[i - 1].split(",");
                prevTerminalName = splitLine[0];
                flowValue = Double.parseDouble(splitLine[2]);
                timestamp = splitLine[3];

                Terminal terminalToAdd = new Terminal(prevTerminalName, flowValue, timestamp);
                String groupToAddTo = TerminalMap.getTerminalGroup(prevTerminalName);
                terminalGroupList.get(groupToAddTo).addTerminal(terminalToAdd);

                if (Objects.equals(terminalName, "Terminal Totals")) {
                    break;
                } else {
                    prevTerminalName = terminalName;
                }
            }
        }

        //Return terminal groups from HashMap as Json
        return ok(Json.toJson(terminalGroupList.values()));
    }


    public WSResponse getCSV() throws Exception {
        // We need to request the CSV file from the national grid website
        //
        // This requires us to do a POST request to their server, passing some hard coded form parameters in the body
        // of the request
        CompletionStage<WSResponse> futureResponse = ws.url("http://energywatch.natgrid.co.uk/EDP-PublicUI/Public/InstantaneousFlowsIntoNTS.aspx?CalledFrom=nguk")
                .setContentType("application/x-www-form-urlencoded")
                .post("__EVENTTARGET=a1&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwULLTEyMjEyNjg2NzlkGAIFDmd2SW5zdGFudFRhYmxlDzwrAAoBCAIBZAUGdGFibGUzDzwrAAoBCAIBZJ7q2q75hRISuNNRLVb%2BQrFJ%2Bj5M&__VIEWSTATEGENERATOR=27867E27&__EVENTVALIDATION=%2FwEWAwL%2Bn5W7DgK%2F7%2BbtDALDmNLsDhsc68eKkomPaNlEXMwXYS14SX%2Bw");

        // Attempt to request the CSV.
        return futureResponse.toCompletableFuture().get();
    }

}
