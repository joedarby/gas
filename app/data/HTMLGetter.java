package data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.libs.ws.WSClient;
import play.libs.ws.WSCookie;
import play.libs.ws.WSResponse;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by Joe on 17/10/2016.
 */
public class HTMLGetter {


    public static Document getHTMLDocument(WSClient ws, String url) throws Exception {

        CompletionStage<WSResponse> response = ws.url(url).get();
        WSResponse wsResponse = response.toCompletableFuture().get();


        return Jsoup.parse(wsResponse.getBody());

    }

    public static Document getNorwayHTMLDocument(WSClient ws, String url, WSCookie cookie) throws Exception {

        CompletionStage<WSResponse> response = ws.url(url)
                .setHeader("Cookie", cookie.getName() + "=" + cookie.getValue())
                .setFollowRedirects(true)
                .get();
        WSResponse wsResponse = response.toCompletableFuture().get();


        return Jsoup.parse(wsResponse.getBody());



    }

    public static List<WSCookie> getCookies(WSClient ws, String url) throws Exception {
        CompletionStage<WSResponse> response = ws.url(url).get();
        WSResponse wsResponse = response.toCompletableFuture().get();
        return wsResponse.getCookies();
    }

}
