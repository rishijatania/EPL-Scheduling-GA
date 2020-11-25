package edu.neu.psa.GA;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.neu.psa.Actors.MasterActor;


public class EplGAParallel {


    public static void main(String[] args) {
        final ActorSystem actorSystem = ActorSystem.create("Epl-GA-System");
        ActorRef masterActor = actorSystem.actorOf(MasterActor.props());
        masterActor.tell(new MasterActor.Init(), null);


    }


}
