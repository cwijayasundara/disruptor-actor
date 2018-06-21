package com.cham.disruptor.loadtest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.lmax.disruptor.EventHandler;

public class TradeEventHandler implements EventHandler<Trade> {

    ActorSystem actorSystem = ActorSystem.create("hazelcastRepoActor");

    ActorRef actorRef = actorSystem.actorOf(HazelcastRepositoryActor.props("test123"), "test");

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {

        actorRef.tell(event, actorRef);

    }
}
