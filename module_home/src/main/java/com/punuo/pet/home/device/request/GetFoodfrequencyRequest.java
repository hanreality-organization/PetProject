package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetFoodfrequencyRequest extends BaseRequest<ChartData> {
    public GetFoodfrequencyRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/weightshistogram/getEatFrequency");
    }
}
