package com.punuo.sys.app.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.QueryResponse;
import com.punuo.sip.request.SipMediaRequest;
import com.punuo.sip.request.SipQueryRequest;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.BaseHandler;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_FRAGMENT)
public class VideoFragment extends BaseFragment implements BaseHandler.MessageHandler,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        TextureView.SurfaceTextureListener{

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.back)
    View mBack;
    @BindView(R2.id.surface)
    TextureView mTextureView;
    @BindView(R2.id.play)
    Button mPlay;
    @BindView(R2.id.stop)
    Button mStop;

    private MediaPlayer mMediaPlayer;
    private Surface surface;
    private String devId;
    private MyAsyncTask mAsyncTask;
    private BaseHandler mBaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.video_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        mBaseHandler = new BaseHandler(this);
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        devId = "310023005801930001";
        EventBus.getDefault().register(this);
        return mFragmentView;
    }

    private void initView() {
        mBack.setVisibility(View.GONE);
        initTitle();
        initSubTitle();
        initTextureView();
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在获取视频...");
                mAsyncTask = new MyAsyncTask(getActivity());
                mAsyncTask.execute();
            }
        });
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseMediaPlayer();
            }
        });
    }

    private void initTextureView() {
        int width = CommonUtil.getWidth();
        mTextureView.getLayoutParams().height = H264Config.VIDEO_WIDTH * width /H264Config.VIDEO_HEIGHT ;//H264Config.VIDEO_HEIGHT
        mTextureView.setRotation(180);
        mTextureView.setSurfaceTextureListener(this);
    }

    private void initSubTitle() {
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("全屏");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 全屏播放
//                startVideo(devId);
                ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
                        .navigation();
            }
        });
    }

    private void initTitle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        String title = simpleDateFormat.format(new Date());
        mTitle.setText(title);
    }


    private void queryMediaInfo(final String devId) {
        SipQueryRequest sipQueryRequest = new SipQueryRequest(devId);
        SipUserManager.getInstance().addRequest(sipQueryRequest);
    }

    private void inviteMedia(String devId) {
        SipMediaRequest sipMediaRequest = new SipMediaRequest(devId);
        SipUserManager.getInstance().addRequest(sipMediaRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(QueryResponse event) {
        inviteMedia(devId);
    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(getActivity(), true);
            mMediaPlayer.setDataSource("rtmp://101.69.255.130:1936/hls/live");
            if (surface == null) {
                surface = new Surface(surfaceTexture);
            }
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            if (getActivity() != null) {
                getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }

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
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                playVideo(mTextureView.getSurfaceTexture());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        dismissLoadingDialog();
        mMediaPlayer.start();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

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

    class MyAsyncTask extends AsyncTask<Object, Object, Boolean> {
        private Context mContext;

        public MyAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            return Vitamio.initialize(mContext,
                    getResources().getIdentifier("libarm", "raw", mContext.getPackageName()));
        }

        @Override
        protected void onPostExecute(Boolean inited) {
            if (inited) {
                mBaseHandler.sendEmptyMessage(0);
            }
        }
    }
}
