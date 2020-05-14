package com.punuo.pet.home.device.request;

import com.punuo.pet.home.device.model.ChartData;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetFoodFrequencyRequest extends BaseRequest<ChartData> {

    public GetFoodFrequencyRequest(ChartType chartType) {
        setRequestType(RequestType.GET);
        if (chartType == ChartType.DAY) {
            setRequestPath("/weightshistogram/getDayEatFrequency");
        } else if (chartType == ChartType.WEEK) {
            setRequestPath("/weightshistogram/getWeekEatFrequency");
        } else if (chartType == ChartType.MONTH) {
            setRequestPath("/weightshistogram/getMonthEatFrequency");
        }
    }
}
