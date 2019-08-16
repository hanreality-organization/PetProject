package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.DeviceModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class GetBindDeviceRequest extends BaseRequest<DeviceModel> {
    public GetBindDeviceRequest() {
        setRequestType(RequestType.GET);
        setRequestPath("/devs/getBindedDevInfo");
    }
}
