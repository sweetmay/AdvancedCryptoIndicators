package com.sweetmay.advancedcryptoindicators.request_coingeckoBTC;

import android.util.Log;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultFnG;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetCryptoData {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/coins/";
    private Response<CoinGeckoRequest> coinGeckoResponse;
    private CoinGeckoHistoryCall geckoHistory;
    private CallBackOnResultFnG callBackOnResultFnG;

    public GetCryptoData(CallBackOnResultFnG callBackOnResultFnG){
        this.callBackOnResultFnG = callBackOnResultFnG;
        initRetrofit();
    }

    private void initRetrofit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        geckoHistory = retrofit.create(CoinGeckoHistoryCall.class);
    }

    public void requestHistory(String coin, String currencyAgainst, String days){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<CoinGeckoRequest> response = geckoHistory.loadHistory(coin, currencyAgainst, days).execute();
                    if(response.body() != null && response.isSuccessful()){
                        coinGeckoResponse =  response;
                        callBackOnResultFnG.onResultCoinGecko(coinGeckoResponse);
                    }else throw new IOException();
                }catch (IOException e){
                    Log.d("DebugLogs", String.valueOf(e));
                }
            }
        }).start();
    }
}
