package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;

public class SaveBathTimeRequest extends BaseRequest {

    public SaveBathTimeRequest(){
        setRequestPath("/");
        setRequestType(RequestType.POST);
    }
}
