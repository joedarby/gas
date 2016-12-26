package data;

import com.fasterxml.jackson.databind.JsonNode;

public class LinepackDataSet {
    public String OLPDate;
    public String PCLPTime;
    public Double OLP;
    public Double PCLP;
    public Double forecastDemand;
    public Double forecastFlow;
    public Boolean oversupply;
    public Double sysImbalance;


    public LinepackDataSet(JsonNode json) {
        OLP = json.get("actualDataModel").get("actualLinepackDataModel").get("actualLinepackOpeningValue").get("value").doubleValue();
        OLPDate = json.get("actualDataModel").get("actualLinepackDataModel").get("actualLinepackOpeningValue").get("applicableFor").textValue().substring(0,10);
        PCLP = json.get("forecastViewModel").get("predictedClosingLinePackValue").get("value").doubleValue();
        PCLPTime = json.get("forecastViewModel").get("predictedClosingLinePackValue").get("applicableAt").textValue().substring(11,16);
        forecastDemand = json.get("forecastViewModel").get("forecastDemandTodayValue").get("value").doubleValue();
        forecastFlow = json.get("forecastViewModel").get("forecastFlowValue").get("value").doubleValue();


        oversupply = PCLP >= OLP;

        sysImbalance = PCLP - OLP;

    }
}
