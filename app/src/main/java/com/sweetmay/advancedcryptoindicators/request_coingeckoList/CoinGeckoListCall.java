package com.sweetmay.advancedcryptoindicators.request_coingeckoList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinGeckoListCall {
    @GET("markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false&price_change_percentage=1h")
    Call<List<ListDatum>> loadList();
}
