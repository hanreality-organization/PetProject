package com.punuo.sip.request;

import com.punuo.sip.SipConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

/**
 * Created by han.chen.
 * Date on 2019-08-21.
 **/
public class SipQueryRequest extends BaseSipRequest {
    private String mDevId;

    public SipQueryRequest(String devId) {
        setSipRequestType(SipRequestType.Options);
        this.mDevId = devId;
        setTargetResponse("query_response");
    }

    @Override
    public String getBody() {
        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();
        try {
            value.put("variable", "MediaInfo_Video");
            value.put("dev_type", 2);
            body.put("query", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();
    }

    @Override
    public NameAddress getDestNameAddress() {
        String devID = mDevId.substring(0, mDevId.length() - 4).concat("0160"); //设备id后4位替换成0160
        SipURL sipURL = new SipURL(devID, SipConfig.getServerIp(), SipConfig.getPort());
        return new NameAddress(devID, sipURL);
    }
}
