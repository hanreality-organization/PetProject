package com.punuo.sys.app.video.stream;

import com.punuo.sip.model.MediaData;
import com.punuo.sip.model.QueryData;

/**
 * Created by han.chen.
 * Date on 2019-09-18.
 **/
public class H264Config {
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

    private static String rtpIp;
    private static int rtpPort;
    private static byte[] magic;

    public static void initQueryData(QueryData queryData) {

    }

    public static void initMediaData(MediaData mediaData) {

    }

    public static byte[] getMagic() {
        return magic;
    }
}
