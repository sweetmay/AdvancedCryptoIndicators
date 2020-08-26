package com.sweetmay.advancedcryptoindicators.request_coingeckoBTC;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinGeckoHistoryCall {
    @GET("{coin}/market_chart")
    Call<CoinGeckoRequest> loadHistory(@Path("coin") String coin, @Query ("vs_currency") String currencyAgainst, @Query("days") String days);
}
