package com.punuo.sip.request;

import com.punuo.sip.H264Config;
import com.punuo.sip.SipConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

/**
 * Created by han.chen.
 * Date on 2020/6/3.
 *
 **/
public class SipOptionsRequest extends BaseSipRequest {
    public SipOptionsRequest() {
        setSipRequestType(SipRequestType.Options);
        setTargetResponse("query_response");
    }

    @Override
    public NameAddress getDestNameAddress() {
        String finalDevId = H264Config.devId.substring(0, H264Config.devId.length() - 4).concat("0160"); //设备id后4位替换成0160
        SipURL sipURL = new SipURL(finalDevId, SipConfig.getServerIp(), SipConfig.getPort());
        return new NameAddress("device", sipURL);
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


}
