package com.sweetmay.advancedcryptoindicators;

import com.github.mikephil.charting.data.LineData;

public class BounceEntity {

    private String coin;
    private String indicator;
    private String imageUrl;

    public BounceEntity(String coin, String indicator, String imageUrl){
        this.coin = coin;
        this.indicator = indicator;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
