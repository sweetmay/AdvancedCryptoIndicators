
package com.sweetmay.advancedcryptoindicators.request_fng;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("value_classification")
    @Expose
    private String valueClassification;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("time_until_update")
    @Expose
    private String timeUntilUpdate;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueClassification() {
        return valueClassification;
    }

    public void setValueClassification(String valueClassification) {
        this.valueClassification = valueClassification;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimeUntilUpdate() {
        return timeUntilUpdate;
    }

    public void setTimeUntilUpdate(String timeUntilUpdate) {
        this.timeUntilUpdate = timeUntilUpdate;
    }

}
