package com.punuo.sip.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by han.chen.
 * Date on 2019-09-29.
 **/
public class DevNotifyData {
    @SerializedName("login")
    public DevInfo mDevInfo;

    public static class DevInfo {
        @SerializedName("devid")
        public String devId;

        @SerializedName("live")
        public int live;
    }
}
