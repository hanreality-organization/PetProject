package com.punuo.pet.member.request;

import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetIdRequest extends BaseRequest<GetIdModel> {

    public GetIdRequest(){
        setRequestPath("/users/visitMall");
        setRequestType(RequestType.GET);
    }
}
