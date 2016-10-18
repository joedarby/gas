package data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class NorwayDataSet {

    public HashMap<String, Double> norwayFlows = new HashMap<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    public Date timestamp;

    public NorwayDataSet(Document doc) {

        timestamp = Calendar.getInstance().getTime();


        Elements flows = doc.getElementsByClass("flow");

        for (Element flow : flows) {
            String heading = flow.getElementsByClass("heading").text();
            Double flowVal = Double.parseDouble(flow.getElementsByClass("value").text());

            switch (heading) {
                case "Entry SEGAL Pipeline System": {
                    norwayFlows.put("SEGAL", flowVal);
                    break;
                }
                case "St. Fergus": {
                    norwayFlows.put("Vesterled", flowVal);
                    break;
                }
                case "Easington": {
                    norwayFlows.put("Langeled", flowVal);
                    break;
                }
                default: norwayFlows.put(heading,flowVal);
            }



        }

    }
}
