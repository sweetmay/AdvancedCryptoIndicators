package com.sweetmay.advancedcryptoindicators.request_fng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FnGCall {
    @GET("fng/")
    Call<FngData> loadFnG(@Query("limit") String limit);
}
