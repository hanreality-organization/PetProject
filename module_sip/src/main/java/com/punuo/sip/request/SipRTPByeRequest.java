package com.punuo.sip.request;

import com.punuo.sip.H264Config;
import com.punuo.sip.SipConfig;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

/**
 * Created by han.chen.
 * Date on 2019-09-21.
 **/
public class SipRTPByeRequest extends BaseSipRequest {

    public SipRTPByeRequest() {
        setSipRequestType(SipRequestType.Bye);
        setHasResponse(false);
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public NameAddress getDestNameAddress() {
        String finalDevId = H264Config.devId.substring(0, H264Config.devId.length() - 4).concat("0160"); //设备id后4位替换成0160
        SipURL sipURL = new SipURL(finalDevId, SipConfig.getServerIp(), SipConfig.getPort());
        return new NameAddress("device", sipURL);
    }
}
