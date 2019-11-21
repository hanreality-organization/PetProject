package com.punuo.pet.feed.plan;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

import java.util.List;

public class PlanModel extends BaseModel {

    @SerializedName("feedplans")
     public List<Plan> mPlanList;

}
