package com.punuo.sys.app.video.rtp;


import java.net.DatagramSocket;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;

/**
 * Created by han.chen.
 * Date on 2020/6/15.
 **/
public class RtpVideo implements RTPAppIntf {
    private final byte H264_STREAM_HEAD[] = {0x00, 0x00, 0x00, 0x01};
    private RTPSession rtpSession;
    private DatagramSocket rtpSocket;
    private byte[] tempNal = new byte[200000];
    private int tempNalLen = 0;
    private int putNum;
    private int preSeq = -1;
    private boolean isPacketLost = true;

    @Override
    public void receiveData(DataFrame frame, Participant participant) {

    }

    @Override
    public void userEvent(int type, Participant[] participant) {

    }

    @Override
    public int frameSize(int payloadType) {
        return 0;
    }
}
