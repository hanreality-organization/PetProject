package com.punuo.sip.video;

/**
 * Created by han.chen.
 * Date on 2019-09-18.
 **/
public class MediaSample {
    public static final int TOTAL_PAGE = 0;
    public static final int FIRST_PAGE = 1;
    public static final int CENTER_PAGE = 2;
    public static final int LAST_PAGE = 3;


    private int mediaStatus = 1;
    private NalBuffer[] mNalBuffers;
    private int putNum;

    private static MediaSample mediaSample;

    public static MediaSample getInstance() {
        if (mediaSample == null) {
            synchronized (MediaSample.class) {
                if (mediaSample == null) {
                    mediaSample = new MediaSample();
                }
            }
        }
        return mediaSample;
    }

    private MediaSample() {
        mNalBuffers = new NalBuffer[200];
        for (int i = 0; i < 200; i++) {
            mNalBuffers[i] = new NalBuffer();
        }
        putNum = 0;
    }

    public void setMediaStatus(int mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public int getMediaStatus() {
        return mediaStatus;
    }

    public void setNalBuf(byte[] tempNal, int tempNalLen) {
        mNalBuffers[putNum].setNalBuf(tempNal, tempNalLen);
        mNalBuffers[putNum].writeLock();
        putNum++;
        putNum = putNum % 200;
    }

    public void jumpNal() {
        mNalBuffers[putNum].writeLock();
        putNum++;
        putNum = putNum % 200;
    }
}
