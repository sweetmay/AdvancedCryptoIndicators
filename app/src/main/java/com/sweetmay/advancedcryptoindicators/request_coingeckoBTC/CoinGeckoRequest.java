
package com.sweetmay.advancedcryptoindicators.request_coingeckoBTC;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinGeckoRequest {

    @SerializedName("prices")
    @Expose
    private List<List<Float>> prices = null;
    @SerializedName("market_caps")
    @Expose
    private List<List<Float>> marketCaps = null;
    @SerializedName("total_volumes")
    @Expose
    private List<List<Float>> totalVolumes = null;

    public List<List<Float>> getPrices() {
        return prices;
    }

    public void setPrices(List<List<Float>> prices) {
        this.prices = prices;
    }

    public List<List<Float>> getMarketCaps() {
        return marketCaps;
    }

    public void setMarketCaps(List<List<Float>> marketCaps) {
        this.marketCaps = marketCaps;
    }

    public List<List<Float>> getTotalVolumes() {
        return totalVolumes;
    }

    public void setTotalVolumes(List<List<Float>> totalVolumes) {
        this.totalVolumes = totalVolumes;
    }

}
