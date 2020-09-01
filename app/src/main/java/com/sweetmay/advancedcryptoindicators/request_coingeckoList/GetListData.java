package com.sweetmay.advancedcryptoindicators.request_coingeckoList;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetListData {

    private final static String BASE_URL = "https://api.coingecko.com/api/v3/coins/";
    private CoinGeckoListCall coinGeckoListCall;
    private onResultListCallBack onResultListCallBack;

    public GetListData(){
        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coinGeckoListCall = retrofit.create(CoinGeckoListCall.class);
    }

    public void requestList(onResultListCallBack onResultListCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<ListDatum>> response = coinGeckoListCall.loadList().execute();
                    if(response.body() != null && response.isSuccessful()){
                        onResultListCallBack.onResult(response);
                    }else throw new IOException();
                }catch (IOException e){
                    Log.d("DebugLogs", String.valueOf(e) + "joopa");
                }
            }
        }).start();
    }
}
