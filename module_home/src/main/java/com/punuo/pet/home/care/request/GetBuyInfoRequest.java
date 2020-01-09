package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetBuyInfoRequest extends BaseRequest <AlarmInfoModel>{
    public GetBuyInfoRequest(){
        setRequestPath("/dailycare/getPetFood");
        setRequestType(RequestType.GET);
    }
}
