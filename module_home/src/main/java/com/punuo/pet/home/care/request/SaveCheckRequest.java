package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveCheckRequest extends BaseRequest<BaseModel> {
    public SaveCheckRequest(){
        setRequestPath("/dailycare/addHealthExam");
        setRequestType(RequestType.POST);
    }
}
