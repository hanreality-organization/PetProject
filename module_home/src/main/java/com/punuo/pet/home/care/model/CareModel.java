package com.punuo.pet.home.care.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class CareModel {

    @SerializedName("data")
    public List<CareData> mCareDataList;
}
