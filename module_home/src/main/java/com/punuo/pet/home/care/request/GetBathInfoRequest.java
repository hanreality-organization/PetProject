package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetBathInfoRequest extends BaseRequest<AlarmInfoModel> {
    public GetBathInfoRequest(){
        setRequestPath("/dailycare/getShowerPlan");
        setRequestType(RequestType.GET);
    }
}
