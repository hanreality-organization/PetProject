package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetVaccinreInfoRequest extends BaseRequest<AlarmInfoModel>{
    public GetVaccinreInfoRequest(){
        setRequestPath("/dailycare/getTakeVaccine");
        setRequestType(RequestType.GET);
    }
}
