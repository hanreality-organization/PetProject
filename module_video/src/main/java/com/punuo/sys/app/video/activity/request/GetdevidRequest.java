package com.punuo.sys.app.video.activity.request;

import com.punuo.sys.app.video.activity.model.deviddata;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetdevidRequest extends BaseRequest<deviddata>{
    public GetdevidRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/groups/getDevidByUsername");
    }
}
