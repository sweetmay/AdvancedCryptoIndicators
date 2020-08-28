package com.sweetmay.advancedcryptoindicators;

import android.util.Log;

import java.util.List;

public class RSICalculator {

    public static int period = 14;


    public static float calculateRSI(List<Float> prices){
        return 100 - (100/ (1+calculateRS(prices)));
    }

    private static float calculateRS(List<Float> prices){
        float AvgGain = 0;
        float AvgLoss = 0;
        int j = period;
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
