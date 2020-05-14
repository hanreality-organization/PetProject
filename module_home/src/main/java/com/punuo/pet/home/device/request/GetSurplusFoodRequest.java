package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData3;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetSurplusFoodRequest extends BaseRequest<ChartData3> {
    public GetSurplusFoodRequest(ChartType chartType) {
        setRequestType(RequestType.GET);
        if (chartType == ChartType.DAY) {
            setRequestPath("/weightshistogram/getDayLeftedWeight");
        } else if (chartType == ChartType.WEEK) {
            setRequestPath("/weightshistogram/getWeekLeftedWeight");
        } else if (chartType == ChartType.MONTH) {
            setRequestPath("/weightshistogram/getMonthLeftedWeight");
        }
    }
}
