
package com.sweetmay.advancedcryptoindicators.request_fng;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("error")
    @Expose
    private Object error;

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
