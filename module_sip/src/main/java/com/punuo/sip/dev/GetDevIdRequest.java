package com.punuo.sip.dev;

import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetDevIdRequest extends BaseRequest<DevData>{
    public GetDevIdRequest(){
        setRequestType(RequestType.GET);
        setRequestPath("/groups/getDevidByUsername");
    }
}
