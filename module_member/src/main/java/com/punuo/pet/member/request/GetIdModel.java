package com.punuo.pet.member.request;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

public class GetIdModel extends BaseModel {
    @SerializedName("id")
    public int shopId;
}
