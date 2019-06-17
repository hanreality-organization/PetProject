package com.punuo.pet.login.request;

import com.punuo.pet.login.model.LoginResult;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 * 微信登陆
 **/
public class WeChatLoginRequest extends BaseRequest<LoginResult> {
    public WeChatLoginRequest() {
        setRequestType(RequestType.GET);
        setRequestPath("/wechatLogin");
    }
}
