package com.cham.disruptor.loadtest;

import com.lmax.disruptor.EventTranslator;

public class TradeEventTranslator implements EventTranslator<Trade> {

    private Trade inTrade;

    public void setInTrade(Trade inTrade) {
        this.inTrade = inTrade;
    }

    @Override
    public void translateTo(Trade event, long sequence) {
        event.tradeId = inTrade.tradeId;
        event.tradeName = inTrade.tradeName;
        event.tradePrice = inTrade.tradePrice;
    }
}
