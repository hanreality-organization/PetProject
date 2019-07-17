package com.punuo.pet.member.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 **/
public class LogoutRequest extends BaseRequest<BaseModel> {

    public LogoutRequest() {
        setRequestPath("/logout");
        setRequestType(RequestType.POST);
    }
}
