package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveWalkRequest extends BaseRequest<BaseModel>{
    public SaveWalkRequest(){
        setRequestPath("/dailycare/addWalkPet");
        setRequestType(RequestType.POST);
    }
}
