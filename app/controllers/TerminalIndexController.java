package controllers;

import data.ConvertTimestamp;
import data.Pipeline;
import data.Terminal;
import data.TerminalMap;
import database.GasDatabase;
import play.db.Database;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;


public class TerminalIndexController extends Controller {

    // The WSClient is injected automatically by the framework
    // The WSClient is used to do HTTP requests to other services (eg, to grab our CSV)
    @Inject
    private WSClient ws;

    @Inject
    Database database;

    public Result terminalIndex() {
        ArrayList<String> csvLines;

        try {
            csvLines = getAndSplitTerminalCSV();
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

        List<Terminal> finalTerminals = getTerminals(csvLines);

        new GasDatabase(database).CheckAndAddToDatabase(finalTerminals);

        //Return terminals from HashMap as Json
        return ok(Json.toJson(finalTerminals));


    }

    private List<Terminal> getTerminals(ArrayList<String> csvLines) {
        HashMap<String, Terminal> terminalList = TerminalMap.initiateTerminalList();

        // Look at the first line in the returned csv lines
        String prevPipelineName = csvLines.get(0).split(",")[0];
        String prevLine = csvLines.get(0);

        // Look through each line in the csv. When the terminal name changes, add the last line for the current terminal (most recent data) to the terminal list
        for (String line : csvLines) {
            String pipelineName = line.split(",")[0];
            if (!Objects.equals(prevPipelineName, pipelineName)) {
                String[] splitLine = prevLine.split(",");
                prevPipelineName = splitLine[0];
                Double flowValue = Double.parseDouble(splitLine[2]);
                Date timestamp = ConvertTimestamp.rawTimestampToFormattedTimestamp(splitLine[3]);

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
            }
            prevPipelineName = pipelineName;
            prevLine = line;
        }

        List<Terminal> finalTerminals = new ArrayList<>(terminalList.values());
        Collections.sort(finalTerminals);
        return finalTerminals;

    }

    private ArrayList<String> getAndSplitTerminalCSV() throws Exception {
        // Call the requestCSVFile method returning response
        WSResponse response;
        response = requestCSVFile();
        String body = response.getBody();
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(body.split("\n")));
        lines.remove(0);
        int endIndex = 0;
        for (String line : lines) {
            if (line.startsWith("Terminal")) {
                endIndex = lines.indexOf(line);
            }
        }
        lines.subList(endIndex + 1, lines.size()).clear();
        return lines;
    }


    private WSResponse requestCSVFile() throws Exception {
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
