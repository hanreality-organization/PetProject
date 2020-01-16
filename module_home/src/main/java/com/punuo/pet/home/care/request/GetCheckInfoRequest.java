package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class GetCheckInfoRequest extends BaseRequest<AlarmInfoModel> {
    public GetCheckInfoRequest() {
        setRequestPath("/dailycare/getHealthExam");
        setRequestType(RequestType.GET);
    }
}
