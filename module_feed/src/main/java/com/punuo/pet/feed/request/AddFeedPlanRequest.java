package com.punuo.pet.feed.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 * 新增喂食计划
 **/
public class AddFeedPlanRequest extends BaseRequest<BaseModel> {
    public AddFeedPlanRequest() {
        setRequestPath("/feedings/addFeedingPlan");
        setRequestType(RequestType.POST);
    }
}
