package com.sweetmay.advancedcryptoindicators;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.List;

public class RSICalculator {

    public static int period = 14;
    private HandlerThread calculationThread;
    private Handler handler;

    public RSICalculator(){
        calculationThread = new HandlerThread("InnerRSI");
        calculationThread.start();
        handler = new Handler(calculationThread.getLooper());
    }

    public void calculateRSI(List<Float> prices, OnRSIResultCallBack onRSIResultCallBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onRSIResultCallBack.onResultRSI(String.valueOf(100 - (100/ (1+calculateRS(prices)))));
            }
        });
    }

    private float calculateRS(List<Float> prices){
        float AvgGain = 0;
        float AvgLoss = 0;
        int upNum = 0;
        int downNum = 0;

        for(int i = 0; i < prices.size()-period; i++){
            if(i ==0){
                continue;
            }
            if(prices.get(i) > prices.get(i-1)){
                AvgGain+= prices.get(i) - prices.get(i-1);
                upNum++;
            }
            if(prices.get(i) < prices.get(i-1)) {
                AvgLoss+= prices.get(i-1) - prices.get(i);
                downNum++;
            }
    }
        if(upNum!=0){
            AvgGain/=upNum;
        }
        if(downNum!=0){
            AvgLoss/=downNum;
        }

        for (int i = prices.size()-period; i < prices.size(); i++) {
            if(prices.get(i)>prices.get(i-1)){
                AvgGain = (AvgGain*(period-1) + prices.get(i)-prices.get(i-1))/14;
            }
            if(prices.get(i)<prices.get(i-1)){
                AvgLoss = (AvgLoss*(period-1) + prices.get(i-1)-prices.get(i))/14;
            }
        }

        return AvgGain/AvgLoss;
}
}
