package com.punuo.pet.feed.plan;

public class Plan {

    private String planTime;
    private String planName;
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
