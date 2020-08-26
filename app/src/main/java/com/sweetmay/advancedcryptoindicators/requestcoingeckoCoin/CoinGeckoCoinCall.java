package com.sweetmay.advancedcryptoindicators.requestcoingeckoCoin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinGeckoCoinCall {
    @GET("{coin}/market_chart")
    Call<CoinData> loadCoin(@Path("coin") String coinId, @Query("vs_currency") String currencyAgainst, @Query("days") String days);
}
