
package com.sweetmay.advancedcryptoindicators.request_fng;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FngData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
