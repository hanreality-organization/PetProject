package com.punuo.sip.request;

import com.punuo.sip.SipConfig;
import com.punuo.sip.model.RegisterData;
import com.punuo.sys.sdk.sercet.SHA1;

import org.json.JSONException;
import org.json.JSONObject;
import org.zoolu.sip.address.NameAddress;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 * 提交密码
 **/
public class SipRegisterRequest extends BaseSipRequest<Object> {
    private RegisterData mRegisterData;
    public SipRegisterRequest(RegisterData data) {
        setSipRequestType(SipRequestType.Register);
        mRegisterData = data;
    }

    @Override
    public NameAddress getDestNameAddress() {
        return SipConfig.getServerAddress();
    }

    @Override
    public NameAddress getLocalNameAddress() {
        return SipConfig.getUserNormalAddress();
    }

    @Override
    public String getBody() {
        if (mRegisterData == null || mRegisterData.mNegotiateResponse == null) {
            return null;
        }
        RegisterData.NegotiateResponse response = mRegisterData.mNegotiateResponse;
        //TODO 快捷登陆的账号如果没有设置过密码的 会sip注册失败,后续改成密码是固定的即可。
        String password = SHA1.getInstance().hashData(response.salt + "123456");
        password = SHA1.getInstance().hashData(response.seed + password);
        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();
        try {
            value.put("password", password);
            body.put("login_request", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();
    }
}
