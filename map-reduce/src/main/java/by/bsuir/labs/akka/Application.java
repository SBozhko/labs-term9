package by.bsuir.labs.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import by.bsuir.labs.akka.actor.MasterActor;
import by.bsuir.labs.akka.message.Result;

public class Application {

    public static void main(String[] args) throws Exception {

        ActorSystem _system = ActorSystem.create("MapReduceApp");

        ActorRef master = _system.actorOf(new Props(MasterActor.class), "master");

        master.tell("Actors are very lightweight concurrent entities");
        master.tell("Actors are very lightweight concurrent entities");
        master.tell("Actors are very lightweight");

        Thread.sleep(500);

        master.tell(new Result());

        Thread.sleep(500);

        _system.shutdown();
    }
}
