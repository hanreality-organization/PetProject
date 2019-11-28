package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData3;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetSurplusfoodRequest extends BaseRequest<ChartData3> {
    public GetSurplusfoodRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/weightshistogram/getLeftedWeight");
    }
}
