package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveBuyRequest extends BaseRequest<BaseModel> {
    public SaveBuyRequest(){
        setRequestPath("/dailycare/addPetFood");
        setRequestType(RequestType.POST);
    }
}
