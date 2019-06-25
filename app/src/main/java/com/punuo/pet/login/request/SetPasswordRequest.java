package com.punuo.pet.login.request;

import com.punuo.pet.model.BaseModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

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
