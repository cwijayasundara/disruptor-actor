package com.cham.disruptor.loadtest;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.jet.IMapJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.memory.MemorySize;
import com.hazelcast.memory.MemoryUnit;


public class HazelcastRepositoryActor extends UntypedActor {

    private final String message;

    NativeMemoryConfig memoryConfig = new NativeMemoryConfig().setSize(new MemorySize(4,MemoryUnit.GIGABYTES)).setEnabled(true);

    ClientConfig config = new ClientConfig().setNativeMemoryConfig(memoryConfig);
    JetInstance jetInstance = Jet.newJetClient(config);

    public HazelcastRepositoryActor(String message) {
        this.message = message;
    }

    static public Props props(String message) {
        return Props.create(HazelcastRepositoryActor.class, () -> new HazelcastRepositoryActor(message));
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        Trade trade = (Trade) message;

        System.out.println("trade = " + trade.toString());

        // insert into hazelcast
        IMapJet<Long, Trade> map = jetInstance.getMap("trade-map");
        map.set(trade.tradeId, trade);
        //System.out.println("map.size() = " + map.size());
    }
}
