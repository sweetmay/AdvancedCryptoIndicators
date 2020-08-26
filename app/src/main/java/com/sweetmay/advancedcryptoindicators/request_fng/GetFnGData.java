package com.sweetmay.advancedcryptoindicators.request_fng;

import android.util.Log;

import com.sweetmay.advancedcryptoindicators.CallBackOnResultFnG;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetFnGData {
    private static final String BASE_URL = "https://api.alternative.me/";
    private FnGCall fng;
    private Response<FngData> responseFnG;
    private CallBackOnResultFnG callBackOnResult;

    public GetFnGData(CallBackOnResultFnG callBackOnResult){
        initRetrofit();
        this.callBackOnResult = callBackOnResult;
    }

    private void initRetrofit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fng = retrofit.create(FnGCall.class);
    }

    public void requestFnG(String limit){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<FngData> response = fng.loadFnG(limit).execute();
                    if(response.body() != null && response.body().getMetadata().getError() == null){
                        responseFnG = response;
                        callBackOnResult.onResultFnG(responseFnG);
                    }else throw new IOException();
                }catch (IOException e){
                    Log.d("DebugLogs", String.valueOf(e));
                }
            }
        }).start();
    }
}
