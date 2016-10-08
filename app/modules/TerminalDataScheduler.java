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
                Duration.create(30, TimeUnit.SECONDS), //Initial delay 0 milliseconds
                Duration.create(20, TimeUnit.SECONDS),     //Frequency 10 seconds
                new Runnable() {
                    @Override
                    public void run() {
                        TerminalIndexController.DataRefresh(wsClient, database);
                        Logger.info("DataRefreshed at " + new Date().toString());
                    }
                },
                actorSystem.dispatcher()
        );
    }

}