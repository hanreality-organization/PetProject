package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveOuterRequest extends BaseRequest<BaseModel> {
    public SaveOuterRequest(){
        setRequestPath("/dailycare/addOuterInsect");
        setRequestType(RequestType.POST);
    }
}
