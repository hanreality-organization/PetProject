package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.CareModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetCareRequest extends BaseRequest<CareModel> {

    public GetCareRequest(){
        setRequestPath("");
        setRequestType(RequestType.GET);
    }
}
