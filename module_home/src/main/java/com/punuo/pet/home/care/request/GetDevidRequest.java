package com.punuo.pet.home.care.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

public class GetDevidRequest extends BaseRequest<BaseModel> {
    public GetDevidRequest(){
        setRequestPath("/groups/getDevidByUsername");
        setRequestType(RequestType.GET);
    }
}
