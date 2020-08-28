package com.sweetmay.advancedcryptoindicators;

import com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin.CoinData;

import retrofit2.Response;

public interface CallBackOnResultCoinData {
    void onCoinResult(String coin, Response<CoinData> response);
}
