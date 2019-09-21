package com.punuo.sip.request;

import com.punuo.sip.SipConfig;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

/**
 * Created by han.chen.
 * Date on 2019-09-21.
 **/
public class SipByeRequest extends BaseSipRequest<Object> {

    private String mDevId;

    public SipByeRequest(String devId) {
        setSipRequestType(SipRequestType.Bye);
        mDevId = devId;
    }

    @Override
    public NameAddress getDestNameAddress() {
        String devID = mDevId.substring(0, mDevId.length() - 4).concat("0160"); //设备id后4位替换成0160
        SipURL sipURL = new SipURL(devID, SipConfig.getServerIp(), SipConfig.getPort());
        return new NameAddress(devID, sipURL);
    }
}
