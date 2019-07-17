package com.punuo.pet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by han.chen.
 * Date on 2019-07-11.
 **/
public class PetData {

    /**
     * petname : miaomiao
     * avatar : http://pet.qinqingonline.com/uploads/17816890870/397a339c578e4095f451c5da04826d3b.jpg
     * type : 1
     * age : 1
     * breed : 1
     * weight : 15.2
     * unit : kg
     * birth : 2019-1-1
     */

    @SerializedName("petname")
    public String petname;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("type")
    public int type;
    @SerializedName("age")
    public int age;
    @SerializedName("breed")
    public int breed;
    @SerializedName("weight")
    public double weight;
    @SerializedName("unit")
    public String unit;
    @SerializedName("birth")
    public String birth;
}
