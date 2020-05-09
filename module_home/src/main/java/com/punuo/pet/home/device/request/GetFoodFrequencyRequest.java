package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetFoodFrequencyRequest extends BaseRequest<ChartData> {
    public GetFoodFrequencyRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/weightshistogram/getEatFrequency");
    }
}
