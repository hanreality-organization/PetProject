package com.punuo.pet.home.device.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

public class ChartData3 extends BaseModel {
    @SerializedName("eat")
    public LeftData leftData;
    public static class LeftData{
        @SerializedName("lefted1")
        public String lefted1;
        @SerializedName("time1")
        public String time1;
        @SerializedName("lefted2")
        public String lefted2;
        @SerializedName("time2")
        public String time2;
        @SerializedName("lefted3")
        public String lefted3;
        @SerializedName("time3")
        public String time3;
        @SerializedName("lefted4")
        public String lefted4;
        @SerializedName("time4")
        public String time4;
    }
}
