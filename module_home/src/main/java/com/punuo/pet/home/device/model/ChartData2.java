package com.punuo.pet.home.device.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

public class ChartData2 extends BaseModel {
    @SerializedName("eat")
    public EatData eatdata;
    public static class EatData{
        @SerializedName("eat1")
        public int eat1;
        @SerializedName("time1")
        public String time1;
        @SerializedName("eat2")
        public int eat2;
        @SerializedName("time2")
        public String time2;
        @SerializedName("eat3")
        public int eat3;
        @SerializedName("time3")
        public String time3;
        @SerializedName("eat4")
        public int eat4;
        @SerializedName("time4")
        public String time4;
    }
}
