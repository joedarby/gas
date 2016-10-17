package data;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class NorwayDataSet {

    public HashMap<String, Double> norwayFlows = new HashMap<>();

    public NorwayDataSet(Document doc) {

        Elements flows = doc.getElementsByClass("flow");

        for (Element flow : flows) {
            String heading = flow.getElementsByClass("heading").text();
            Double flowVal = Double.parseDouble(flow.getElementsByClass("value").text());
            norwayFlows.put(heading,flowVal);

        }

    }
}
