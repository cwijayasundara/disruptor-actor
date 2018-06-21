package com.cham.disruptor.loadtest;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.jet.IMapJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;

public class HazelcastManager {

    public static void main(String[] args) {
        JetInstance jetInstance = Jet.newJetClient(new ClientConfig());
        IMapJet<Long, Trade> map = jetInstance.getMap("trade-map");

        System.out.println("map size :: "+map.size());
    }
}
