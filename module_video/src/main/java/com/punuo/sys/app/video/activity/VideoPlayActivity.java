package com.punuo.sys.app.video.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.widget.media.IjkVideoView;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
public class VideoPlayActivity extends BaseSwipeBackActivity {
    private static final String TAG = "VideoPlayActivity";
    @BindView(R2.id.video_view)
    IjkVideoView mIjkVideoView;
    @BindView(R2.id.video_close_button)
    ImageView mVideoCloseButton;
    @Autowired(name = "url")
    String url;

    private AndroidMediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        init();
    }

    private void init() {
        mVideoCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMediaController = new AndroidMediaController(this, false);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mIjkVideoView.setMediaController(mMediaController);
        mIjkVideoView.setVideoPath(url);
        mIjkVideoView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIjkVideoView.stopPlayback();
        mIjkVideoView.release(true);
        mIjkVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIjkVideoView.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIjkVideoView.stopPlayback();
    }
}
