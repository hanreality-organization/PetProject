package com.punuo.pet.home.care.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class CareData {

    /**
     * icon :
     * label : 洗澡清洁
     * petName :
     * date :
     * needAlarm : false
     * repeatCycle : day
     */

    @SerializedName("icon")
    public String icon;
    @SerializedName("label")
    public String label;
    @SerializedName("petName")
    public String petName;
    @SerializedName("date")
    public long date;
    @SerializedName("needAlarm")
    public boolean needAlarm;
    @SerializedName("repeatCycle")
    public String repeatCycle;
}
