package com.punuo.sys.app.video.rtp;

import android.view.Surface;

import java.lang.ref.WeakReference;

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

    private RtpVideo mRtpVideo;
    private MediaDecoder mMediaDecoder;
    private VideoThread mVideoThread;

    public void init(String networkAddress, int remoteRtpPort) {
        try {
            mRtpVideo = new RtpVideo(networkAddress, remoteRtpPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaDecoder = new MediaDecoder();
    }

    public boolean previewing = false;

    public void setActivePacket(byte[] activePacket) {
        mRtpVideo.setActivePacket(activePacket);
    }

    public void startPreviewVideo(Surface surface) {
        previewing = true;
        mVideoThread = new VideoThread(surface);
        mVideoThread.start();
    }

    public void stopPreviewVideo() {
        previewing = false;
        mRtpVideo.removeParticipant();
        mRtpVideo.endSession();
        if (mMediaDecoder != null) {
            mMediaDecoder.release();
        }
    }

    class VideoThread extends Thread {
        private int getNum = 0;
        private WeakReference<Surface> mWeakReference;

        VideoThread(Surface surface) {
            mWeakReference = new WeakReference<>(surface);
        }

        @Override
        public void run() {
            super.run();
            if (mWeakReference.get() != null) {
                if (mMediaDecoder != null) {
                    mMediaDecoder.initDecoder(mWeakReference.get());
                }
                while (previewing) {
                    if (mWeakReference.get() != null) {
                        byte[] nal = RtpVideo.nalBuffers[getNum].getReadableNalBuf();
                        if (nal != null) {
                            try {
                                mMediaDecoder.onFrame(nal, 0, nal.length);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        RtpVideo.nalBuffers[getNum].readLock();
                        RtpVideo.nalBuffers[getNum].cleanNalBuf();
                        getNum++;
                        getNum %= 200;
                    }
                }
            }
        }
    }
}
