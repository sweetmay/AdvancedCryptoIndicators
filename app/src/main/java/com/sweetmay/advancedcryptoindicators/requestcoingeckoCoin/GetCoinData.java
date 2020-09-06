package com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.RequiresApi;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultCoinData;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

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

    public void requestCoinData(Map<String, String> coins, String days, CallBackOnResultCoinData callBackOnResultCoinData){
                Iterator<Map.Entry<String, String>> iter = coins.entrySet().iterator();
                while (iter.hasNext()){
                    Map.Entry<String, String> pair = iter.next();
                    try {
                        Response<CoinData> response = coinCall.loadCoin(pair.getKey(), "usd", days).execute();
                        if(response.isSuccessful()){
                            callBackOnResultCoinData.onCoinResult(pair, response);
                        }else requestCoinFail(callBackOnResultCoinData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                callBackOnResultCoinData.onDone();

    }

    public void requestCoinData(String coin, String days, CallBackOnResultCoinData callBackOnResultCoinData){
        receiverHandler.post(new Runnable() {
            @Override
            public void run() {
                    try {
                        Response<CoinData> response = coinCall.loadCoin(coin, "usd", days).execute();
                        if(response.isSuccessful()){
                            callBackOnResultCoinData.onCoinResult(coin, response);
                        }else requestCoinFail(callBackOnResultCoinData);
                    } catch (IOException e) {
                        e.printStackTrace();
                }
            }
        });
    }

    public void requestCoinFail(CallBackOnResultCoinData callBackOnFailCoin){
        callBackOnFailCoin.onCoinFail();
    }

}
