package com.punuo.sip.request;

import com.punuo.sip.SipConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

/**
 * Created by han.chen.
 * Date on 2019-08-17.
 **/
public class SipControlDeviceRequest extends BaseSipRequest<Object> {
    private String mOperate;

    public SipControlDeviceRequest(String operate) {
        setSipRequestType(SipRequestType.Notify);
        mOperate = operate;
    }

    @Override
    public NameAddress getDestNameAddress() {
        SipURL remote = new SipURL("321000000200150001", SipConfig.getServerIp(), SipConfig.getPort());
        return new NameAddress("321000000200150001", remote);
    }

    @Override
    public String getBody() {
        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();
        try {
            value.put("operate", mOperate);
            body.put("direction_control", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();
    }
}
