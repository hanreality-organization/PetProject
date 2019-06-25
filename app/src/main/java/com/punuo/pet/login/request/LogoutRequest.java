package com.punuo.pet.login.request;

import com.punuo.pet.model.BaseModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 **/
public class LogoutRequest extends BaseRequest<BaseModel> {

    public LogoutRequest() {
        setRequestPath("/logout");
        setRequestType(RequestType.GET);
    }
}
