package com.punuo.pet.home.device.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;


public class ChartData extends BaseModel {
    @SerializedName("eat")
    public FrequencyData frequencyData;
    public static class FrequencyData {
        @SerializedName("frequency1")
        public int frequency1;
        @SerializedName("time1")
        public String time1;
        @SerializedName("frequency2")
        public int frequency2;
        @SerializedName("time2")
        public String time2;
        @SerializedName("frequency3")
        public int frequency3;
        @SerializedName("time3")
        public String time3;
        @SerializedName("frequency4")
        public int frequency4;
        @SerializedName("time4")
        public String time4;
    }
}
