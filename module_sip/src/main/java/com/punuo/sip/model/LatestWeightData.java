package com.punuo.sip.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

public class LatestWeightData extends BaseModel {
    @SerializedName("weight")
    public String latestWeight;
}
