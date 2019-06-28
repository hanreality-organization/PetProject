package com.punuo.pet.member.login.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 * 获取验证码请求
 **/
public class GetCodeRequest extends BaseRequest<BaseModel> {
    public GetCodeRequest() {
        setRequestType(RequestType.POST);
        setRequestPath("/sendCode");
    }
}
