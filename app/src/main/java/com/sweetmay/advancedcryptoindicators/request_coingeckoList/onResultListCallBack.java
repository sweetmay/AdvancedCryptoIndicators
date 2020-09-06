package com.sweetmay.advancedcryptoindicators.request_coingeckoList;

import java.util.List;

import retrofit2.Response;

public interface onResultListCallBack {
    void onResultList(Response<List<ListDatum>> listDataResponse);

}
