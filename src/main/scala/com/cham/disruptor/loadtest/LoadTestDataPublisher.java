package com.cham.disruptor.loadtest;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadTestDataPublisher {


    public static void main(String[] args) {

        int ring_size = 1024 * 128;
        ExecutorService executor =  Executors.newCachedThreadPool();


        BlockingWaitStrategy blockingWaitStrategy = new BlockingWaitStrategy();
        BusySpinWaitStrategy busySpinWaitStrategy = new BusySpinWaitStrategy();

        EventFactory<Trade> tradeEventFactory = new EventFactory<Trade>(){

            @Override
            public Trade newInstance() {
                return new Trade(1, "trade1", 2.5);
            }
        };

        TradeEventHandler tradeEventHandler = new TradeEventHandler();

        Disruptor<Trade> tradeDisruptor = new Disruptor<>(tradeEventFactory, ring_size, executor, ProducerType.SINGLE, busySpinWaitStrategy);
        tradeDisruptor.handleEventsWith(tradeEventHandler);

        tradeDisruptor.start();

        TradeEventTranslator tradeEventTranslator = new TradeEventTranslator();
        for (int i = 1; i < 1000000; i++) {

            Trade trade = new Trade(i, "IRSTrade" + i, (double) i * 2);
            tradeEventTranslator.setInTrade(trade);
            tradeDisruptor.publishEvent(tradeEventTranslator);
        }
    }




//    val blockingWaitStrategy:BlockingWaitStrategy = new BlockingWaitStrategy
//
//    val busySpinWaitStretegy:BusySpinWaitStrategy = new BusySpinWaitStrategy
//
//    val factory = new EventFactory[ValueEventTweet] {
//        override def newInstance(): ValueEventTweet = ValueEventTweet(0L,"0","0")
//    }
//    // instance of the handler
//    val handler = new ValueEventTweetHandler
//
//    val disruptor = new Disruptor[ValueEventTweet](factory,ring_size,executor,ProducerType.SINGLE,busySpinWaitStretegy)
//            // link disruptor to handler
//            disruptor.handleEventsWith(handler)
//
//            disruptor.start()
//    val inTweetToDisruptor: ValueEventTweet = new ValueEventTweet(1,"I love Scala", "Chaminda W")
//
//    val start = Instant.now
//    println("started..")
//
//    for (i <- 1 to 10) {
//        // publish events to discruptor that will be handed by the handler
//        disruptor.publishEvent(ValueEventTweetTranslator(inTweetToDisruptor))
//    }

}
