package data;

import org.jsoup.nodes.Document;
import play.Logger;

/**
 * Created by Joe on 20/09/2016.
 */
public class LinepackDataSet {
    public String OLPDate;
    public String PCLPTime;
    public Double OLP;
    public Double PCLP;
    public Double forecastDemand;
    public Double forecastFlow;
    public Boolean oversupply;
    public Double sysImbalance;


    public LinepackDataSet(Document doc) {
        OLP = Double.parseDouble(doc.getElementsByAttributeValueContaining("data-bind","OpeningValue").get(0).text());
        OLPDate = doc.getElementById("ctl00_cphActual_lblOpeningdt").text();
        PCLP = Double.parseDouble(doc.getElementById("ctl00_cphForecast_tdPredicted1").text());
        PCLPTime = doc.getElementById("ctl00_cphForecast_tdPredicted2").text();
        PCLPTime = PCLPTime.substring(1, PCLPTime.length()-1);
        forecastDemand = Double.parseDouble(doc.getElementById("ctl00_cphForecast_tdDemandD").text());
        forecastFlow = Double.parseDouble(doc.getElementById("ctl00_cphForecast_tdForecastFlow1").text());

        oversupply = PCLP >= OLP;

        sysImbalance = PCLP - OLP;

    }
}
