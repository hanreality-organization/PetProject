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

//    @SerializedName("icon")
//    public String icon;
    @SerializedName("carename")
    public String careName;
    @SerializedName("petname")
    public String petName;
    @SerializedName("date")
    public long date;
//    @SerializedName("needAlarm")
//    public boolean needAlarm;
//    @SerializedName("repeatCycle")
//    public String repeatCycle;

    public CareData(String icon,String label,String petName,long date){
//        this.icon = icon;
        this.careName = label;
        this.petName = petName;
        this.date = date;
    }

    public String getIcon(){
        return "";
    }
    public String getCareName(){
        return careName;
    }
    public String getPetName(){
        return petName;
    }
    public long getDate(){
        return date;
    }
}
