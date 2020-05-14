package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData2;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetFoodNumberRequest extends BaseRequest<ChartData2> {
    public GetFoodNumberRequest(ChartType chartType) {
        setRequestType(RequestType.GET);
        if (chartType == ChartType.DAY) {
            setRequestPath("/weightshistogram/getDayEatWeight");
        } else if (chartType == ChartType.WEEK) {
            setRequestPath("/weightshistogram/getWeekEatWeight");
        } else if (chartType == ChartType.MONTH) {
            setRequestPath("/weightshistogram/getMonthEatWeight");
        }
    }
}
