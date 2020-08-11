package com.punuo.pet.feed.request;

import com.punuo.pet.feed.model.GetRemainderModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetRemainderRequest extends BaseRequest<GetRemainderModel> {
    public GetRemainderRequest(){
        setRequestPath("/weightshistogram/getDayLeftedWeight");
        setRequestType(RequestType.GET);
    }
}
