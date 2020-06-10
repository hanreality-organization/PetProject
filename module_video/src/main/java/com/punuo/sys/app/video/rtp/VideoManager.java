package com.punuo.sys.app.video.rtp;

import android.util.Log;

/**
 * Created by han.chen.
 * Date on 2020/6/10.
 **/
public class VideoManager {

    private static VideoManager videoManager;
    public static VideoManager getInstance() {
        if (videoManager == null) {
            synchronized (VideoHeartBeatHelper.class) {
                if (videoManager == null) {
                    videoManager = new VideoManager();
                }
            }
        }
        return videoManager;
    }

    public void setActivePacket(byte[] activePacket) {
        Log.i("han.chen", "发送视频流心跳包");
    }
}
