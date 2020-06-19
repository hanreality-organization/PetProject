package com.punuo.sys.app.video.rtp;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.view.Surface;

import com.punuo.sip.H264Config;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by han.chen.
 * Date on 2020/6/19.
 **/
public class MediaDecoder {
    private static final String TAG = "MediaDecoder";
    private MediaCodec mMediaCodec;
    private static final String MIME_TYPE = "video/avc";
    private final static int TIME_INTERNAL = 10;
    private int count = 0;

    public void initDecoder(Surface surface) {
        try {
            mMediaCodec = MediaCodec.createDecoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, H264Config.VIDEO_WIDTH, H264Config.VIDEO_HEIGHT);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        mMediaCodec.configure(mediaFormat, surface, null, 0);
        mMediaCodec.start();
    }

    public boolean onFrame(byte[] buffer, int offset, int length) {
        ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(buffer, offset, length);
            mMediaCodec.queueInputBuffer(inputBufferIndex, 0, length, count * TIME_INTERNAL, 0);
            count++;
        } else {
            return false;
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        while (outputBufferIndex >= 0) {
            mMediaCodec.releaseOutputBuffer(outputBufferIndex, true);
            //释放缓冲区的时候需要注意第二个参数设置为true，表示解码显示在Surface上
            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
        return true;
    }
}
