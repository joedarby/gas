package modules;

import akka.actor.*;
import controllers.NorwayController;
import controllers.TerminalIndexController;
import play.Logger;
import play.db.Database;
import play.libs.ws.WSClient;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Singleton
public class NorwayDataScheduler {

    @Inject
    public NorwayDataScheduler(ActorSystem actorSystem, WSClient wsClient, Database database) {
        actorSystem.scheduler().schedule(
                Duration.create(10, TimeUnit.SECONDS), //Initial delay 10 seconds
                Duration.create(30, TimeUnit.SECONDS),     //Frequency 30 seconds
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            NorwayController.DataRefresh(wsClient, database);
                            Logger.info("Norway data refreshed at " + new Date().toString());
                        } catch (Exception e) {
                            Logger.error("Get norway data failed" + new Date().toString());
                        }
                    }
                },
                actorSystem.dispatcher()
        );
    }

}