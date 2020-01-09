package com.punuo.pet.home.care.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

import java.util.List;

public class AlarmInfoModel extends BaseModel {

        @SerializedName("time")
        public long time;
        @SerializedName("remind")
        public String remind;
        @SerializedName("period")
        public String period;
}
