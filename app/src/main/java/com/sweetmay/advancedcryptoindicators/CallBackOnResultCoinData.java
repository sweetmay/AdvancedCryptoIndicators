package com.sweetmay.advancedcryptoindicators;

import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;

import java.util.Map;

import retrofit2.Response;

public interface CallBackOnResultCoinData {
    void onCoinResult(String coin, Response<CoinData> response);
    void onCoinResult(Map.Entry<String, String> coinAndImage, Response<CoinData> response);
    void onCoinFail();
}
