package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetOuterInfoRequest extends BaseRequest <AlarmInfoModel>{
    public GetOuterInfoRequest(){
        setRequestPath("/dailycare/getOuterInsect");
        setRequestType(RequestType.GET);
    }
}
