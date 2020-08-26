package com.sweetmay.advancedcryptoindicators;

import com.sweetmay.advancedcryptoindicators.request_coingeckoBTC.CoinGeckoRequest;
import com.sweetmay.advancedcryptoindicators.request_fng.FngData;

import retrofit2.Response;

public interface CallBackOnResultFnG {
    void onResultFnG(Response<FngData> fngRequestResponse);
    void onResultCoinGecko(Response<CoinGeckoRequest> coinGeckoRequestResponse);
}
