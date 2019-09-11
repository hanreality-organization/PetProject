package com.punuo.pet.home.feed.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 * 更新喂食计划
 **/
public class UpdateFeedPlanRequest extends BaseRequest<BaseModel> {
    public UpdateFeedPlanRequest() {
        setRequestPath("/feedings/updateFeedingPlan");
        setRequestType(RequestType.POST);
    }
}
