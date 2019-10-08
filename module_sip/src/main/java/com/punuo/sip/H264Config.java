package com.punuo.sip;

import com.punuo.sip.model.MediaData;
import com.punuo.sip.model.QueryResponse;

/**
 * Created by han.chen.
 * Date on 2019-09-18.
 **/
public class H264Config {
    public static final byte[] SPS_MOBILE_DEVICE = {0x00, 0x00, 0x00, 0x01, 0x67, 0x4D, 0x40, 0x14, 0x14, 0x60, 0x60, 0x03,
            0x08, 0x00, 0x00, 0x03, 0x00, 0x08, 0x00, 0x00, 0x03, 0x00, 0x0c, 0x78, 0x5F, 0x4c, 0x50, };
    public static final byte[] PPS_MOBILE_DEVICE = {0x00, 0x00, 0x00, 0x01, 0x68, 0x32, 0x3c, (byte)0x80};
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

    public static void initQueryData(QueryResponse queryData) {

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
