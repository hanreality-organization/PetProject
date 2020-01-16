package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetBeautyInfoRequest extends BaseRequest <AlarmInfoModel>{
    public GetBeautyInfoRequest(){
        setRequestPath("/dailycare/getMakeBeauty");
        setRequestType(RequestType.GET);
    }
}
