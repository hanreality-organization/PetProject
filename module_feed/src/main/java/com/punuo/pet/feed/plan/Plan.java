package com.punuo.pet.feed.plan;

import com.google.gson.annotations.SerializedName;

public class Plan {

    @SerializedName("time")
    private String planTime;
    @SerializedName("name")
    private String planName;
    @SerializedName("count")
    private String planCount;

    public Plan(String planTime,String planName,String planCount){
        this.planTime = planTime;
        this.planName = planName;
        this.planCount = planCount;
    }

    public String getPlanTime() {
        return planTime;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPlanCount() {
        return planCount;
    }
}
