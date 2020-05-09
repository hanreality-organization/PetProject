package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData2;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetFoodNumberRequest extends BaseRequest<ChartData2> {
    public GetFoodNumberRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/weightshistogram/getEatWeight");
    }
}
