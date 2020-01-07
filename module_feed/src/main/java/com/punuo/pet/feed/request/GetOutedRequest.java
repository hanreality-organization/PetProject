package com.punuo.pet.feed.request;

import com.punuo.pet.feed.model.OutedModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetOutedRequest extends BaseRequest<OutedModel> {
    public GetOutedRequest(){
        setRequestPath("/");
        setRequestType(RequestType.GET);
    }
}
