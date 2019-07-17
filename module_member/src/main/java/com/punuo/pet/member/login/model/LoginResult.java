package com.punuo.pet.member.login.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class LoginResult extends BaseModel {
    @SerializedName("session")
    public String session;

    @SerializedName("hasBindPhone")
    public boolean hasBindPhone;
}
