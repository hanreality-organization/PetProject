package com.punuo.pet.home.feed.request;

import com.punuo.pet.home.feed.model.FeedPlanModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 * 获取喂食计划接口
 **/
public class GetFeedPlanRequest extends BaseRequest<FeedPlanModel> {
    public GetFeedPlanRequest() {
        setRequestPath("/feedings/getFeedingPlan");
        setRequestType(RequestType.GET);
    }
}
