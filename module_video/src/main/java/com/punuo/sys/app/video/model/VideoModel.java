package com.punuo.sys.app.video.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

import java.util.List;

public class VideoModel extends BaseModel {
    @SerializedName("videourl")
    public List<String> videolist;
}
