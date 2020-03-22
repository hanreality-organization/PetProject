package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData3;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetSurplusFoodRequest extends BaseRequest<ChartData3> {
    public GetSurplusFoodRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/weightshistogram/getLeftedWeight");
    }
}
