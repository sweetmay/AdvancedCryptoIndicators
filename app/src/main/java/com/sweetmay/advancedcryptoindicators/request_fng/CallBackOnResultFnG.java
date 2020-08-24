package com.sweetmay.advancedcryptoindicators.request_fng;

import retrofit2.Response;

public interface CallBackOnResultFnG {
    void onResult(Response<FngRequest> fngRequestResponse);
}
