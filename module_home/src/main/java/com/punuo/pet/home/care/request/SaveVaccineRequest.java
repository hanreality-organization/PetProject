package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class SaveVaccineRequest extends BaseRequest<BaseModel> {
    public SaveVaccineRequest(){
        setRequestPath("/dailycare/addTakeVaccine");
        setRequestType(RequestType.POST);
    }
}
