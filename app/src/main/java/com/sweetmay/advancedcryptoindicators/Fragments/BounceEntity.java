package com.sweetmay.advancedcryptoindicators.Fragments;

import com.github.mikephil.charting.data.LineData;

public class BounceEntity {

    private String coin;
    private String indicator;

    public BounceEntity(String coin, String indicator){
        this.coin = coin;
        this.indicator = indicator;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }
}
