package com.punuo.sys.app.video.activity;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.view.Surface;
import android.view.TextureView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipByeRequest;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 *
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
public class VideoPlayActivity extends BaseActivity implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener {
    private static final String TAG = "VideoPlayActivity";
    private static final int MSG_CHECK_VIDEO = 2;
    private static final int DELAY = 20000;
    @BindView(R2.id.surface)
    TextureView mTextureView;
    private MediaPlayer mMediaPlayer;
    private Surface surface;

    private boolean mIsVideoReadyToBePlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        initSurfaceViewSize();
    }

    private void initSurfaceViewSize() {
        int width = CommonUtil.getWidth();
        mTextureView.getLayoutParams().height = H264Config.VIDEO_WIDTH * width / H264Config.VIDEO_HEIGHT;
        mTextureView.setRotation(180);
        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        closeVideo();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    private void closeVideo() {
        SipByeRequest sipByeRequest = new SipByeRequest("310023005801930001");
        SipUserManager.getInstance().addRequest(sipByeRequest);
        finish();
    }

    private void doCleanUp() {
        mIsVideoReadyToBePlayed = false;
    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        doCleanUp();
        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(this, true);
            mMediaPlayer.setDataSource("rtmp://101.69.255.130:1936/hls/live");
            if (surface == null) {
                surface = new Surface(surfaceTexture);
            }
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        playVideo(surface);
        showLoadingDialog();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        dismissLoadingDialog();
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed) {
            startVideoPlayback();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    private void startVideoPlayback() {
        mMediaPlayer.start();
    }
}
