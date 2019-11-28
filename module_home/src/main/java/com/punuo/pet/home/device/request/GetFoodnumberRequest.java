package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData2;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetFoodnumberRequest extends BaseRequest<ChartData2> {
    public GetFoodnumberRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/weightshistogram/getEatWeight");
    }
}
