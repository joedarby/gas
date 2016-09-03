package controllers;

import data.Terminal;
import data.TerminalGroup;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletionStage;


public class HomeController extends Controller {

    // The WSClient is injected automatically by the framework
    // The WSClient is used to do HTTP requests to other services (eg, to grab our CSV)
    @Inject WSClient ws;

    public Result index() {

        // Create terminal to terminal group mapping
        HashMap terminalMap = new HashMap();
        terminalMap.put("BACTON BBL", "BACTON IP");
        terminalMap.put("BACTON IC", "BACTON IP");
        terminalMap.put("BACTON PERENCO", "BACTON UKCS");
        terminalMap.put("BACTON SEAL", "BACTON UKCS");
        terminalMap.put("BACTON SHELL", "BACTON UKCS");
        terminalMap.put("ST FERGUS MOBIL", "ST FERGUS");
        terminalMap.put("ST FERGUS NSMP", "ST FERGUS");
        terminalMap.put("ST FERGUS SHELL", "ST FERGUS");
        terminalMap.put("TEESSIDE PX", "TEESSIDE");
        terminalMap.put("TEESSIDE BP", "TEESSIDE");
        terminalMap.put("ALDBROUGH", "MEDIUM RANGE STORAGE");
        terminalMap.put("HILLTOP", "MEDIUM RANGE STORAGE");
        terminalMap.put("HOLE HOUSE FARM", "MEDIUM RANGE STORAGE");
        terminalMap.put("HOLFORD", "MEDIUM RANGE STORAGE");
        terminalMap.put("HORNSEA", "MEDIUM RANGE STORAGE");
        terminalMap.put("STUBLACH", "MEDIUM RANGE STORAGE");
        terminalMap.put("GRAIN NTS 1", "ISLE OF GRAIN");
        terminalMap.put("GRAIN NTS 2", "ISLE OF GRAIN");
        terminalMap.put("BARROW SOUTH", "BARROW");
        terminalMap.put("THEDDLETHORPE", "THEDDLETHORPE");

        String[] terminalGroupNames = {"BACTON IP", "BACTON UKCS", "BARROW", "ISLE OF GRAIN", "ST FERGUS", "TEESSIDE", "THEDDLETHORPE", "MEDIUM RANGE STORAGE", "OTHER"};



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

        // Create a list of Terminal Groups to return in our response and populate with empty Terminal Groups
        HashMap<String, TerminalGroup> terminalGroupList = new HashMap<>();
        for (String terminalGroupName : terminalGroupNames) {
            TerminalGroup terminalGroup = new TerminalGroup(terminalGroupName);
            terminalGroupList.put(terminalGroupName, terminalGroup);
        }


        // Look at the name of the first terminal in the csv (ignore line 0 which is a header)
        String prevTerminalName = lines[1].split(",")[0];
        String terminalName = "";
        double flowValue = 0;
        String timestamp = "";

        // Look through each line in the csv. When the terminal name changes, add the last line for the current terminal (most recent data) to the terminal list
        for (int i = 1; i < 9999; i++) {
            terminalName = lines[i].split(",")[0];
            if (!Objects.equals(prevTerminalName, terminalName)) {
                prevTerminalName = lines[i - 1].split(",")[0];
                flowValue = Double.parseDouble(lines[i - 1].split(",")[2]);
                timestamp = lines[i - 1].split(",")[3];
                Terminal terminalToAdd = new Terminal(prevTerminalName, flowValue, timestamp);
                String groupToAddTo;
                if (terminalMap.containsKey(prevTerminalName)) {
                    groupToAddTo = (String) terminalMap.get(prevTerminalName);
                } else {
                    groupToAddTo = "Other";
                }
                TerminalGroup targetTerminalGroup = terminalGroupList.get(groupToAddTo);
                terminalGroupList.remove(groupToAddTo);
                targetTerminalGroup.addTerminal(terminalToAdd);
                terminalGroupList.put(groupToAddTo, targetTerminalGroup);
                if (Objects.equals(terminalName, "Terminal Totals")) {
                    break;
                } else {
                    prevTerminalName = terminalName;
                }
            }
        }






        // We convert the list of terminals automagically into JSON
        return ok(Json.toJson(terminalGroupList));
    }

}
