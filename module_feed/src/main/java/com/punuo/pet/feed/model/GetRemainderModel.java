package com.punuo.pet.feed.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

import java.util.List;

public class GetRemainderModel extends BaseModel {
    @SerializedName("eat")
    public eat mRemainder;

    public static class eat{
        @SerializedName("lefted1")
        public String remainder;
        @SerializedName("time1")
        public String time1;
        @SerializedName("lefted2")
        public String remainder2;
        @SerializedName("time2")
        public String time2;
        @SerializedName("lefted3")
        public String remainder3;
        @SerializedName("time3")
        public String time3;
        @SerializedName("lefted4")
        public String remainder4;
        @SerializedName("time4")
        public String time4;
    }
}
