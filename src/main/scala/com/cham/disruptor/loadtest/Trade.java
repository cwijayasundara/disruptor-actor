package com.cham.disruptor.loadtest;

import java.io.Serializable;

public class Trade implements Serializable {

    public long tradeId;
    public String tradeName;
    public Double tradePrice;

    public Trade(long tradeId, String tradeName, Double tradePrice) {
        this.tradeId = tradeId;
        this.tradeName = tradeName;
        this.tradePrice = tradePrice;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", tradePrice='" + tradePrice + '\'' +
                '}';
    }
}
