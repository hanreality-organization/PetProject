package com.punuo.pet.home.device.model;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class DeviceModel extends BaseModel {

    @SerializedName("devInfo")
    public List<DeviceInfo> mDeviceInfoList;
}
