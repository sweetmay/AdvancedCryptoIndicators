package com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin;

import android.os.Handler;
import android.os.HandlerThread;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;

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
    private CallBackOnResultCoinData callBackOnResultCoinData;

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

    public void requestCoinData(LinkedList<String> coins, String days, CallBackOnResultCoinData callBackOnResultCoinData){
        receiverHandler.post(new Runnable() {
            @Override
            public void run() {
                while (coins.size() != 0){
                    try {
                        Response<CoinData> response = coinCall.loadCoin(coins.get(0), "usd", days).execute();
                        if(response.isSuccessful()){
                            callBackOnResultCoinData.onCoinResult(coins.get(0), response);
                            coins.remove(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
