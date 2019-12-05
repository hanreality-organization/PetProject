package com.punuo.pet.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class ChangePwdRequest extends BaseRequest<BaseModel> {

    public ChangePwdRequest(){
        setRequestPath("/users/updatePwd");
        setRequestType(RequestType.POST);
    }
}
