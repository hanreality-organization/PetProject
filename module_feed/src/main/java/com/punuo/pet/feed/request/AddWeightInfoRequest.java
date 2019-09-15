package com.punuo.pet.feed.request;

import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 * 新增称重信息
 **/
public class AddWeightInfoRequest extends BaseRequest {
    public AddWeightInfoRequest() {
        setRequestPath("/weights/addWeight");
        setRequestType(RequestType.POST);
    }
}
