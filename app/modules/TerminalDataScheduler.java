package modules;

import akka.actor.*;
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
public class TerminalDataScheduler {

    @Inject
    public TerminalDataScheduler(ActorSystem actorSystem, WSClient wsClient, Database database) {
        actorSystem.scheduler().schedule(
                Duration.create(30, TimeUnit.SECONDS), //Initial delay 30 seconds
                Duration.create(30, TimeUnit.SECONDS),     //Frequency 30 seconds
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TerminalIndexController.DataRefresh(wsClient, database);
                            Logger.info("UK data refreshed at " + new Date().toString());
                        } catch (Exception e) {
                            Logger.error("Get NG csv file failed" + new Date().toString());
                        }
                    }
                },
                actorSystem.dispatcher()
        );
    }

}