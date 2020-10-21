package com.punuo.pet.home.device.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2020/10/19.
 **/
public class CheckBindDeviceRequest extends BaseRequest<BaseModel> {
    public CheckBindDeviceRequest() {
        setRequestType(RequestType.GET);
        setRequestPath("/devs/isDevBinded");
    }
}
