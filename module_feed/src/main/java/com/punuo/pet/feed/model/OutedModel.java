package com.punuo.pet.feed.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

public class OutedModel extends BaseModel {
    @SerializedName("totalamount")
    public int outedCount;
}
