package com.punuo.sys.app.video.rtp;

import com.punuo.sip.H264Config;

/**
 * Created by han.chen.
 * Date on 2020/6/10.
 **/
public class VideoHeartBeatHelper {
    public static final int DELAY = 20 * 1000;
    private static VideoHeartBeatHelper videoHeartBeatHelper;
    public static VideoHeartBeatHelper getInstance() {
        if (videoHeartBeatHelper == null) {
            synchronized (VideoHeartBeatHelper.class) {
                if (videoHeartBeatHelper == null) {
                    videoHeartBeatHelper = new VideoHeartBeatHelper();
                }
            }
        }
        return videoHeartBeatHelper;
    }
    private byte[] msg = new byte[20];

    public void init() {
        msg[0] = 0x00;
        msg[1] = 0x01;
        msg[2] = 0x00;
        msg[3] = 0x10;
        System.arraycopy(H264Config.getMagic(), 0, msg, 4, 16);
    }
    public void heartBeat() {
        VideoManager.getInstance().setActivePacket(msg);
    }
}
