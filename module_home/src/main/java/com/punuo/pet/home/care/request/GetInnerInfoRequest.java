package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetInnerInfoRequest extends BaseRequest<AlarmInfoModel> {
    public GetInnerInfoRequest(){
        setRequestPath("/dailycare/getInnerInsect");
        setRequestType(RequestType.GET);
    }
}