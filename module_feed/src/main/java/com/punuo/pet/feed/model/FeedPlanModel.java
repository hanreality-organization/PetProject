package com.punuo.pet.feed.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class FeedPlanModel extends BaseModel {
    @SerializedName("feedingPlan")
    public FeedPlanData mFeedPlanData;
}
