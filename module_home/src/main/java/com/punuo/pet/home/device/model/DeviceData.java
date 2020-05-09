package com.punuo.pet.home.device.model;

import com.punuo.sys.sdk.model.PNBaseModel;

/**
 * Created by han.chen.
 * Date on 2020/5/9.
 **/
public class DeviceData extends PNBaseModel {

    public String deviceName;
    public int drawableId;
    public int deviceType;

    public DeviceData(String deviceName, int drawableId, int deviceType) {
        this.deviceName = deviceName;
        this.drawableId = drawableId;
        this.deviceType = deviceType;
    }
}
