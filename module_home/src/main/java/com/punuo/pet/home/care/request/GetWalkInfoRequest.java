package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetWalkInfoRequest extends BaseRequest<AlarmInfoModel> {
    public GetWalkInfoRequest(){
        setRequestPath("/dailycare/getWalkPet");
        setRequestType(RequestType.GET);
    }
}
