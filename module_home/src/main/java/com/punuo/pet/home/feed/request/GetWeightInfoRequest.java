package com.punuo.pet.home.feed.request;

import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 * 获取称重信息
 **/
public class GetWeightInfoRequest extends BaseRequest {
    public GetWeightInfoRequest() {
        setRequestPath("/weights/getWeightInfo");
        setRequestType(RequestType.GET);
    }
}
