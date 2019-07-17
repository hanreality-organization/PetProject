package com.punuo.pet.member.login.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-07-01.
 **/
public class BindPhoneRequest extends BaseRequest<BaseModel> {

    public BindPhoneRequest() {
        setRequestType(RequestType.POST);
        setRequestPath("/wechats/bindPhone");
    }
}
