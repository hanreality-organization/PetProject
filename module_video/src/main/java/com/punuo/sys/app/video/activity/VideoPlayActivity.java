package com.punuo.sys.app.video.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.FFmpeg.ffmpeg;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipByeRequest;
import com.punuo.sip.request.SipRequestListener;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sip.H264Config;
import com.punuo.sys.app.video.stream.MediaRtpReceiver;
import com.punuo.sys.app.video.stream.MediaSample;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.CommonUtil;

import java.net.SocketException;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
public class VideoPlayActivity extends BaseActivity {
    private static final String TAG = "VideoPlayActivity";
    private static final int MSG_CHECK_VIDEO = 2;
    private static final int DELAY = 20000;
    @BindView(R2.id.surface_view)
    SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;
    private ffmpeg mFFmpeg;
    private VideoDecoderThread mVideoDecoderThread;
    private byte[] mPixel = new byte[H264Config.VIDEO_WIDTH * H264Config.VIDEO_HEIGHT * 2];
    private ByteBuffer mVideoBuffer = ByteBuffer.wrap(mPixel);
    private Bitmap mFrameBitmap;
    private int index = 0;
    private MediaRtpReceiver mMediaRtpReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        initSurfaceViewSize();
        mSurfaceHolder = mSurfaceView.getHolder();
        mFFmpeg = new ffmpeg();
        mFFmpeg.Init(H264Config.VIDEO_WIDTH, H264Config.VIDEO_HEIGHT);
        mFFmpeg.DecoderNal(H264Config.SPS_MOBILE_DEVICE, 13, mPixel);
        mFFmpeg.DecoderNal(H264Config.PPS_MOBILE_DEVICE, 9, mPixel);
        mFrameBitmap = Bitmap.createBitmap(H264Config.VIDEO_WIDTH, H264Config.VIDEO_HEIGHT, Bitmap.Config.RGB_565);
        try {
            mMediaRtpReceiver = new MediaRtpReceiver(H264Config.rtpIp, H264Config.rtpPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        playVideo();
    }

    private void initSurfaceViewSize() {
        int width = CommonUtil.getWidth();
        int height = H264Config.VIDEO_HEIGHT * width / H264Config.VIDEO_WIDTH;
        mSurfaceView.getLayoutParams().height = height;
    }

    private void playVideo() {
//        mVideoDecoderThread = new VideoDecoderThread();
//        mVideoDecoderThread.startDecoding();
        if (!mBaseHandler.hasMessages(MSG_CHECK_VIDEO)) {
            mBaseHandler.sendEmptyMessageDelayed(MSG_CHECK_VIDEO, DELAY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mVideoDecoderThread.stopDecoding();
        closeVideo();
        mMediaRtpReceiver.onDestroy();
        try {
            mFFmpeg.Destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mVideoBuffer.clear();
        mBaseHandler.removeMessages(MSG_CHECK_VIDEO);
    }

    class VideoDecoderThread extends Thread {
        private boolean running = false;

        @Override
        public void run() {

            while (running) {
                byte[] nal = MediaSample.getInstance().getReadableNalBuf(index);
                if (nal != null) {
                    try {
                        int iTemp = mFFmpeg.DecoderNal(nal, nal.length, mPixel);
                        if (iTemp > 0) {
                            drawFrameBitmap();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MediaSample.getInstance().readLock(index);
                MediaSample.getInstance().cleanNalBuf(index);
                index++;
                index = index % 200;
            }
        }

        public void startDecoding() {
            running = true;
            start();
        }

        public void stopDecoding() {
            running = false;
            interrupt();
        }
    }

    private void drawFrameBitmap() {
        mFrameBitmap.copyPixelsFromBuffer(mVideoBuffer);
        mVideoBuffer.position(0);
        int surfaceViewWidth = mSurfaceView.getWidth();
        int surfaceViewHeight = mSurfaceView.getHeight();
        int bmpWidth = mFrameBitmap.getWidth();
        int bmpHeight = mFrameBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) surfaceViewWidth / bmpWidth, (float) surfaceViewHeight / bmpHeight);
        Bitmap scaleBitmap = Bitmap.createBitmap(mFrameBitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
        scaleBitmap = adjustPhotoRotation(scaleBitmap, 90);
        if (mSurfaceHolder != null) {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawBitmap(scaleBitmap, 0, 0, null);
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_CHECK_VIDEO:
                if (MediaSample.getInstance().getMediaStatus() == 0) {
                    closeVideo();
                } else {
                    MediaSample.getInstance().setMediaStatus(0);
                }
                mBaseHandler.sendEmptyMessageDelayed(MSG_CHECK_VIDEO, DELAY);
                break;
        }
    }

    private SipByeRequest mSipByeRequest;
    private void closeVideo() {
        mSipByeRequest = new SipByeRequest("310023001139940001");
        mSipByeRequest.setSipRequestListener(new SipRequestListener<Object>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(Object result) {
                finish();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        SipUserManager.getInstance().addRequest(mSipByeRequest);
    }
}
