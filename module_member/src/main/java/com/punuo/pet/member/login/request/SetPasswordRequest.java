package com.punuo.pet.member.login.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-06-22.
 **/
public class SetPasswordRequest extends BaseRequest<BaseModel> {

    public SetPasswordRequest() {
        setRequestPath("/users/setPassWord");
        setRequestType(RequestType.GET);
    }
}
