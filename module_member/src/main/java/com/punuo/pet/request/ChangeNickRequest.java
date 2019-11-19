package com.punuo.pet.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class ChangeNickRequest extends BaseRequest<BaseModel> {

    public ChangeNickRequest(){
        setRequestPath("/users/updateNickname");
        setRequestType(RequestType.POST);
    }
}
