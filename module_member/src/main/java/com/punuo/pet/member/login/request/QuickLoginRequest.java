package com.punuo.pet.member.login.request;

import com.punuo.pet.member.login.model.LoginResult;
import com.punuo.sys.sdk.httplib.BaseRequest;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 * 手机验证码登陆
 **/
public class QuickLoginRequest extends BaseRequest<LoginResult> {

    public QuickLoginRequest() {
        setRequestType(RequestType.GET);
        setRequestPath("/codeLogin");
    }
}
