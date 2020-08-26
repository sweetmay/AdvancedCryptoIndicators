package com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetCoinData {

    private final String BASE_URL =  "https://api.coingecko.com/api/v3/coins/";
    private CoinGeckoCoinCall coinCall;
    private final HandlerThread receiveDataThread;
    private Handler receiverHandler;

    public GetCoinData(){
        initRetrofit();
        receiveDataThread = new HandlerThread("ReceiveDataThread");
        receiveDataThread.start();
        receiverHandler = new Handler(receiveDataThread.getLooper());
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coinCall = retrofit.create(CoinGeckoCoinCall.class);
    }

    public void requestCoinData(LinkedList<String> coins, String days){
        receiverHandler.post(new Runnable() {
            int j = 0;
            @Override
            public void run() {
                while (coins.size() != 0){
                    try {
                        Response<CoinData> response = coinCall.loadCoin(coins.get(0), "usd", days).execute();
                        if(response.isSuccessful()){
                            Log.d("DebugLogs", j + coins.get(0) + ": " + String.valueOf(response.body().getPrices().get(0).get(1)));
                            j++;
                            coins.remove(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("DebugLogs", "Done");
            }
        });
    }

}
