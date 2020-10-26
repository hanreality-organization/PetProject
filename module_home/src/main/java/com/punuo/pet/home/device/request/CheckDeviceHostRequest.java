package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.DeviceHost;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 * 检查是否有主用户
 **/
public class CheckDeviceHostRequest extends BaseRequest<DeviceHost> {
    public CheckDeviceHostRequest() {
        setRequestType(RequestType.GET);
        setRequestPath("/groups/isDevHosted");
    }
}
