package com.punuo.pet.home.feed.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class FeedingAdviceModel extends BaseModel {
    @SerializedName("feedingAdvice")
    public String mFeedingAdvice;
}
