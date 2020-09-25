package com.punuo.sys.app.video.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hengyi.fastvideoplayer.library.FastVideoPlayer;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
public class VideoPlayActivity extends BaseSwipeBackActivity {
    private static final String TAG = "VideoPlayActivity";
    @BindView(R2.id.fast_video_play)
    FastVideoPlayer player;
    @BindView(R2.id.video_close_button)
    ImageView mVideoCloseButton;
    @Autowired(name = "url")
    String url;
    @Autowired(name = "title")
    String title;

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
        int width = CommonUtil.getWidth();
        player.getLayoutParams().height = H264Config.VIDEO_HEIGHT * width / H264Config.VIDEO_WIDTH;
        player.setLive(false);
        player.setScaleType(FastVideoPlayer.SCALETYPE_FITXY);
        player.setShowNavIcon(false);
        player.setTitle(title);
        player.setUrl(url);
        player.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
