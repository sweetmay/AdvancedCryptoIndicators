package com.sweetmay.advancedcryptoindicators;

import java.text.DecimalFormat;

public class BounceEntity {

    private String coin;
    private float indicator;
    private String imageUrl;
    private float probabilityRate;
    private float entryPrice;

    public String getProfit() {
        return "(" + percent.format(profit) + "%)";
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public String getLoss() {
        return "(" + percent.format(loss) + "%)";
    }

    public void setLoss(float loss) {
        this.loss = loss;
    }

    private float stopLoss;
    private float target;
    private float profit;
    private float loss;
    private DecimalFormat decimalFormat;
    private DecimalFormat percent;

    public BounceEntity(String coin, Float indicator, String imageUrl, Float entryPrice){
        decimalFormat = new DecimalFormat("##.######");
        percent = new DecimalFormat("##.##");

        this.entryPrice = entryPrice;
        this.coin = coin;
        this.indicator = indicator;
        this.imageUrl = imageUrl;
        target = calcTarget();
        stopLoss = calcStopLoss();
        probabilityRate = calcProbability();
    }

    private float calcProbability() {
        if(target<entryPrice){
            loss = (stopLoss/entryPrice -1)*100;
            profit = Math.abs(target/entryPrice -1)*100;
            return indicator;
        }else
            loss = Math.abs(entryPrice/stopLoss-1)*100;
            profit = Math.abs(entryPrice/target -1)*100;
            return Math.abs(100 - indicator);
    }

    private Float calcStopLoss() {
        return entryPrice - (target - entryPrice)/2;

    }
    private Float calcTarget() {
        return Math.abs((5 - indicator/10)/100+1)*entryPrice;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getIndicator() {
        return decimalFormat.format(indicator);
    }

    public void setIndicator(float indicator) {
        this.indicator = indicator;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProbabilityRate() {
        return percent.format(probabilityRate);
    }

    public void setProbabilityRate(float probabilityRate) {
        this.probabilityRate = probabilityRate;
    }

    public String getEntryPrice() {
        return decimalFormat.format(entryPrice);
    }

    public void setEntryPrice(float entryPrice) {
        this.entryPrice = entryPrice;
    }

    public String getStopLoss() {
        return decimalFormat.format(stopLoss);
    }

    public void setStopLoss(float stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getTarget() {
        return decimalFormat.format(target);
    }

    public void setTarget(float target) {
        this.target = target;
    }

}
