package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveBathRequest extends BaseRequest<BaseModel> {

    public SaveBathRequest(){
        setRequestPath("/dailycare/addShowerPlan");
        setRequestType(RequestType.POST);
    }
}
