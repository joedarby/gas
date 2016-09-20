package controllers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import play.libs.XPath;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.concurrent.CompletionStage;

/**
 * Created by Joe on 20/09/2016.
 */
public class LinepackController extends Controller{

    @Inject
    private WSClient ws;

    public Result index() {

        byte[] page;
        try {
             page = getPage().asByteArray();
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

        return ok(page);


        /*
        //Code below does not work. Seems treating WSResponse as XML is disallowed.

        Document page;

        try {
            page = getPage().asXml();
        } catch (Exception e) {
            return internalServerError(e.toString());
        }

        Element ele = page.getElementById("c100_cphActual_txtOpening");
        String OLP = ele.toString();

        return ok(OLP);

        */

    }

    private WSResponse getPage() throws Exception {

        CompletionStage<WSResponse> response = ws.url("http://marketinformation.natgrid.co.uk/gas/frmPrevalingView.aspx")
                .get();

        return response.toCompletableFuture().get();
    }

}
