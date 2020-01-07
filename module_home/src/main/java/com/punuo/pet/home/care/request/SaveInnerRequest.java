package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveInnerRequest extends BaseRequest<BaseModel> {
    public SaveInnerRequest(){
        setRequestPath("/dailycare/addInnerInsect");
        setRequestType(RequestType.POST);
    }
}
