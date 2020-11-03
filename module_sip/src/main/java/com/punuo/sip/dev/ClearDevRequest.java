package com.punuo.sip.dev;


import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2020/11/2.
 **/
public class ClearDevRequest extends BaseRequest<ClearDevEvent> {

    public ClearDevRequest() {
        setRequestPath("/devs/clearDev");
        setRequestType(RequestType.GET);
    }
}
