package com.punuo.pet.login.request;

import com.punuo.pet.model.BaseModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 * 获取验证码请求
 **/
public class GetCodeRequest extends BaseRequest<BaseModel> {
    public GetCodeRequest() {
        setRequestType(RequestType.GET);
        setRequestPath("/sendCode");
    }
}
