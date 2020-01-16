package com.punuo.pet.feed.plan;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class DeletePlanRequest extends BaseRequest<BaseModel> {

    public DeletePlanRequest(){
        setRequestPath("/feedplan/deleteFeedPlan");
        setRequestType(RequestType.POST);
    }
}
