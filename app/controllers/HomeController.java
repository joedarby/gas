package controllers;

import data.Pipeline;
import data.Terminal;
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
    @Inject
    private WSClient ws;

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

        String pipelineName;
        Double flowValue;
        String timestamp;
        HashMap<String, Terminal> terminalList = TerminalMap.initiateTerminalList();

      // Look at the name of the first terminal in the csv (ignore line 0 which is a header)
        String prevPipelineName = lines[1].split(",")[0];

        // Look through each line in the csv. When the terminal name changes, add the last line for the current terminal (most recent data) to the terminal list
        for (int i = 1; i < lines.length; i++) {
            pipelineName = lines[i].split(",")[0];
            if (!Objects.equals(prevPipelineName, pipelineName)) {
                String[] splitLine = lines[i - 1].split(",");
                prevPipelineName = splitLine[0];
                flowValue = Double.parseDouble(splitLine[2]);
                timestamp = splitLine[3];

                Pipeline pipelineToAdd = new Pipeline(prevPipelineName, flowValue, timestamp);

                // If the new terminal maps into a terminal group, add to the existing group, otherwise create and add its own group
                if (TerminalMap.getTerminal(prevPipelineName) == null) {
                    Terminal singlePipeline = new Terminal(prevPipelineName);
                    singlePipeline.addPipeline(pipelineToAdd);
                    terminalList.put(prevPipelineName, singlePipeline);
                } else {
                    String groupToAddTo = TerminalMap.getTerminal(prevPipelineName);
                    terminalList.get(groupToAddTo).addPipeline(pipelineToAdd);
                }

                if (Objects.equals(pipelineName, "Terminal Totals")) {
                    break;
                } else {
                    prevPipelineName = pipelineName;
                }
            }
        }

        //
        List<Terminal> finalTerminals = new ArrayList<>(terminalList.values());
        Collections.sort(finalTerminals);

        //Return terminals from HashMap as Json
        return ok(Json.toJson(finalTerminals));
    }


    private WSResponse getCSV() throws Exception {
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
