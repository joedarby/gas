package controllers;

import data.ConvertTimestamp;
import data.Pipeline;
import data.Terminal;
import data.TerminalMap;
import database.UKDatabase;
import play.Logger;
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

        try {
            List<Terminal> finalTerminals = DataRefresh(ws, database);
            return ok(Json.toJson(finalTerminals));
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

    }

    public static List<Terminal> DataRefresh(WSClient wsClient, Database db) throws Exception {
        ArrayList<String> csvLines;
        csvLines = getAndSplitTerminalCSV(wsClient);

        List<Terminal> finalTerminals = getTerminals(csvLines);
        new UKDatabase(db).checkAndAddToDatabase(finalTerminals);

        return finalTerminals;


    }

    private static List<Terminal> getTerminals(ArrayList<String> csvLines) {
        HashMap<String, Terminal> terminalList = TerminalMap.initiateTerminalList();

        // Look at the first line in the returned csv lines
        String prevPipelineName = csvLines.get(0).split(",")[0];
        String prevLine = csvLines.get(0);

        // Look through each line in the csv. When the pipeline name changes, add the last line for the current pipeline (most recent data) to the terminal list
        for (String line : csvLines) {
            String pipelineName = line.split(",")[0];
            if (!prevPipelineName.equals(pipelineName)) {
                Pipeline pipelineToAdd = csvLineToPipeline(prevLine);
                prevPipelineName = pipelineToAdd.pipelineName;

                // If the new pipeline maps into an existing terminal, add to the existing terminal, otherwise create and add its own terminal
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
        for (Terminal terminal : finalTerminals) {
            Collections.sort(terminal.pipelines);
        }
        return finalTerminals;

    }

    private static ArrayList<String> getAndSplitTerminalCSV(WSClient ws) throws Exception {
        // Call the requestCSVFile method returning response
        WSResponse response = requestCSVFile(ws);
        String body = response.getBody();
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(body.split("\n")));
        lines.remove(0); //remove the csv header

        boolean foundTerminalHeader = false;
        Iterator<String> iterator = lines.iterator();
        while(iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("Terminal")) {
                foundTerminalHeader = true;
            }
            // Remove terminal header and anything after it
            if(foundTerminalHeader) {
                iterator.remove();
            }
        }
        lines.add("End Line");
        return lines;
    }

    private static Pipeline csvLineToPipeline (String line) {
        String[] splitLine = line.split(",");
        String prevPipelineName = splitLine[0];
        Double flowValue = Double.parseDouble(splitLine[2]);
        Date timestamp = ConvertTimestamp.rawTimestampToFormattedTimestamp(splitLine[3]);
        return new Pipeline(prevPipelineName, flowValue, timestamp);
    }


    private static WSResponse requestCSVFile(WSClient ws) throws Exception {
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
