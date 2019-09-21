package com.punuo.sip;

import com.punuo.sip.model.MediaData;
import com.punuo.sip.model.QueryData;

/**
 * Created by han.chen.
 * Date on 2019-09-18.
 **/
public class H264Config {
    public static final byte[] SPS_MOBILE_DEVICE = {0x00, 0x00, 0x00, 0x01, 0x27, 0x42, 0x10, 0x09, (byte) 0x96, 0x35, 0x02, (byte) 0x83, (byte) 0xf2};
    public static final byte[] PPS_MOBILE_DEVICE = {0x00, 0x00, 0x00, 0x01, 0x28, (byte) 0xce, 0x02, (byte) 0xfc, (byte) 0x80};
    /**
     * video width
     */
    public static int VIDEO_WIDTH = 320;

    /**
     * video height
     */
    public static int VIDEO_HEIGHT = 240;

    /**
     * video frame rate
     */
    public static int FRAME_RATE = 10;
    /**
     * video type
     */
    public static int VIDEO_TYPE = 2;

    public static String rtpIp = "101.69.255.134";
    public static int rtpPort;
    public static byte[] magic;

    public static void initQueryData(QueryData queryData) {

    }

    public static void initMediaData(MediaData mediaData) {
        rtpIp = mediaData.getIp();
        rtpPort = mediaData.getPort();
        magic = mediaData.getMagic();
    }

    public static byte[] getMagic() {
        return magic;
    }
}
