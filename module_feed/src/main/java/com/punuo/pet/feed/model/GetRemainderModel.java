package com.punuo.pet.feed.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

public class GetRemainderModel extends BaseModel {
    @SerializedName("remain")
    public String mRemainder;
}
