package com.punuo.pet.member.pet.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-07-23.
 **/
public class AddUserInfoRequest extends BaseRequest<BaseModel> {

    public AddUserInfoRequest() {
        setRequestType(RequestType.POST);
        setRequestPath("/users/addUsers");
    }
}
