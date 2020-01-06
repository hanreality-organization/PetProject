package com.punuo.pet.home.view.request;

import com.punuo.pet.home.view.model.RotationChartData;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetRotationChart extends BaseRequest<RotationChartData> {
    public GetRotationChart(){
        setRequestType(RequestType.GET);
        setRequestPath("/getRotationChart");
    }
}
