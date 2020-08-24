package com.sweetmay.advancedcryptoindicators.request_fng;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetFnGData {
    private static final String BASE_URL = "https://api.alternative.me/";
    private FnG fng;
    private Response<FngRequest> responseFnG;
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
        fng = retrofit.create(FnG.class);
    }

    public void requestFnG(String limit){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<FngRequest> response = fng.loadFnG(limit).execute();
                    if(response.body() != null && response.body().getMetadata().getError() == null){
                        responseFnG =  response;
                    }else throw new IOException();
                }catch (IOException e){
                    Log.d("DebugLogs", String.valueOf(e));
                }
                callBackOnResult.onResult(responseFnG);
            }
        }).start();
    }
}
