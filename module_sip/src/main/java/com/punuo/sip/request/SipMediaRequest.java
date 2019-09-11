package com.punuo.sip.request;

import com.punuo.sip.SipConfig;
import com.punuo.sip.model.MediaData;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

/**
 * Created by han.chen.
 * Date on 2019-08-23.
 **/
public class SipMediaRequest extends BaseSipRequest<MediaData> {
    private String mDevId;
    public SipMediaRequest(String devId) {
        setSipRequestType(SipRequestType.Invite);
        mDevId = devId;
    }

    @Override
    public NameAddress getDestNameAddress() {
        String devID = mDevId.substring(0, mDevId.length() - 4).concat("0160"); //设备id后4位替换成0160
        SipURL sipURL = new SipURL(devID, SipConfig.getServerIp(), SipConfig.getPort());
        return new NameAddress(devID, sipURL);
    }

}
