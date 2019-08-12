package com.punuo.sip.request;

import com.punuo.sip.SipConfig;
import com.punuo.sip.model.RegisterData;

import org.zoolu.sip.address.NameAddress;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 * sip注册第一步,根据用户名拿到userId
 **/
public class SipGetUserIdRequest extends BaseSipRequest<RegisterData> {

    public SipGetUserIdRequest() {
        setSipRequestType(SipRequestType.Register);
    }

    @Override
    public NameAddress getLocalNameAddress() {
        return SipConfig.getUserRegisterAddress();
    }
}
