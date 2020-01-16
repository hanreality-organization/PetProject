package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveBeautyRequest extends BaseRequest<BaseModel> {
    public SaveBeautyRequest(){
        setRequestPath("/dailycare/addMakeBeauty");
        setRequestType(RequestType.POST);
    }
}
