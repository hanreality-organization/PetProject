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
 * Date on 2019-08-23.
 **/
public class SipMediaRequest extends BaseSipRequest<String> {
    private String mDevId;

    public SipMediaRequest(String devId) {
        setSipRequestType(SipRequestType.Invite);
        mDevId = devId;
    }

    @Override
    public String getBody() {
        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();
        try {
            value.put("resolution", "Feed_Device");
            value.put("video", "H.264");
            value.put("audio", "G.711");
            value.put("kbps", 800);
            value.put("self", "192.168.1.129 UDP 5200");
            value.put("mode", "active");
            value.put("magic", "01234567890123456789012345678901");
            value.put("dev_type", H264Config.VIDEO_TYPE);
            body.put("media", value);
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
