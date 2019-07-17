package com.punuo.pet.member.login.request;

import com.punuo.pet.member.login.model.LoginResult;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 * 微信登陆
 **/
public class WeChatLoginRequest extends BaseRequest<LoginResult> {
    public WeChatLoginRequest() {
        setRequestType(RequestType.POST);
        setRequestPath("/wechatLogin");
    }
}
