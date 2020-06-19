package com.punuo.sys.app.video;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.device.model.BindDevidSuccess;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.event.VideoFailedEvent;
import com.punuo.sip.model.MediaData;
import com.punuo.sip.model.ResetData;
import com.punuo.sip.model.VolumeData;
import com.punuo.sip.request.SipControlVolumeRequest;
import com.punuo.sip.request.SipOptionsRequest;
import com.punuo.sip.request.SipRTPByeRequest;
import com.punuo.sys.app.video.activity.model.deviddata;
import com.punuo.sys.app.video.activity.request.GetDevIdRequest;
import com.punuo.sys.app.video.audio.AudioRecordManager;
import com.punuo.sys.app.video.rtp.RtpVideo;
import com.punuo.sys.app.video.rtp.VideoHeartBeatHelper;
import com.punuo.sys.app.video.rtp.VideoManager;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.BaseHandler;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_FRAGMENT)
public class VideoFragment extends BaseFragment implements BaseHandler.MessageHandler, SurfaceHolder.Callback {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.back)
    View mBack;
    @BindView(R2.id.surface)
    SurfaceView mSurfaceView;
    @BindView(R2.id.play_status)
    ImageView mPlayStatus;
    @BindView(R2.id.play_music)
    View playMusic;
    @BindView(R2.id.play_video)
    View mPlayVideo;
    @BindView(R2.id.add_voice)
    View add_voice;
    @BindView(R2.id.down_voice)
    View down_voice;

    private String devId;
    private BaseHandler mBaseHandler;
    private boolean isPlaying = false;
    private Timer checkAliveTimer;

    public static final int MSG_VIDEO_HEART_BEAR_VALUE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.video_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        mBaseHandler = new BaseHandler(this);
        //Toast.makeText(getActivity(),"请先确认已绑定设备再获取视频",Toast.LENGTH_LONG).show();
        getDevId();
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        EventBus.getDefault().register(this);
        return mFragmentView;
    }

    private void initView() {
        mBack.setVisibility(View.GONE);
        initTitle();
        initSubTitle();
        initTextureView();
        mPlayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    if (!TextUtils.isEmpty(devId)) {
                        //开始请求视频
                        H264Config.devId = devId;
                        H264Config.numOfTimeOut = 0;
                        startVideo();
                        isPlaying = true;
                    } else {
                        ToastUtils.showToast("请先确认已绑定设备再获取视频");
                    }
                }
            }
        });
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(VideoRouter.ROUTER_MUSIC_CHOOSE_ACTIVITY)
                        .navigation();
            }
        });
        mPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_CHOOSE_ACTIVITY)
                        .navigation();
            }
        });
        down_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SipControlVolumeRequest sipControlVolumeRequest = new SipControlVolumeRequest("lower");
                SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
            }
        });
        add_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SipControlVolumeRequest sipControlVolumeRequest = new SipControlVolumeRequest("raise");
                SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
            }
        });
    }

    private void initTextureView() {
        int width = CommonUtil.getWidth();
        mSurfaceView.getLayoutParams().height = H264Config.VIDEO_HEIGHT * width / H264Config.VIDEO_WIDTH;
        mSurfaceView.getHolder().addCallback(this);
    }

    private void initSubTitle() {
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("停止播放");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeVideo();
                isPlaying = false;
                mPlayStatus.setVisibility(View.VISIBLE);
            }
        });
    }

    private void startVideo() {
        requestVideo();
    }

    private void initTitle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        String title = simpleDateFormat.format(new Date());
        mTitle.setText(title);
    }


    private void requestVideo() {
        SipOptionsRequest optionsRequest = new SipOptionsRequest();
        SipUserManager.getInstance().addRequest(optionsRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收视频
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MediaData event) {
        VideoManager.getInstance().init(H264Config.rtpIp, H264Config.rtpPort);
        VideoHeartBeatHelper.getInstance().init();
        if (!mBaseHandler.hasMessages(MSG_VIDEO_HEART_BEAR_VALUE)) {
            mBaseHandler.sendEmptyMessage(MSG_VIDEO_HEART_BEAR_VALUE);
        }
        VideoManager.getInstance().startPreviewVideo(mSurfaceView.getHolder().getSurface());
        checkAliveTimer = new Timer();
        checkAliveTimer.schedule(task, 0, 10000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoFailedEvent event) {
        isPlaying = false;
        ToastUtils.showToast("请求视频失败，请稍后再试");
    }

    private int time = 0;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (RtpVideo.isReceiveVideoData == 0) {
                if (time == 6) {
                    closeVideo();
                    time = 0;
                } else {
                    time++;
                }
            } else if (RtpVideo.isReceiveVideoData == 2) {
                RtpVideo.isReceiveVideoData = 0;
                time = 0;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResetData result) {
        Toast.makeText(getActivity(), "重置成功", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindDevidSuccess result) {
        getDevId();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VolumeData result) {
        if (TextUtils.equals(result.volume, "raise")) {
            Toast.makeText(getActivity(), "音量加", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.equals(result.volume, "lower")) {
            Toast.makeText(getActivity(), "音量减", Toast.LENGTH_SHORT).show();
        }
    }

    private GetDevIdRequest mGetDevIdRequest;

    public void getDevId() {
        if (mGetDevIdRequest != null && !mGetDevIdRequest.isFinish()) {
            return;
        }
        mGetDevIdRequest = new GetDevIdRequest();
        mGetDevIdRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetDevIdRequest.setRequestListener(new RequestListener<deviddata>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(deviddata result) {
                if (result == null) {
                    return;
                }
                devId = result.devid;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetDevIdRequest);
    }

    private void closeVideo() {
        SipRTPByeRequest sipRTPByeRequest = new SipRTPByeRequest();
        SipUserManager.getInstance().addRequest(sipRTPByeRequest);
        //停止视频解码
        VideoManager.getInstance().stopPreviewVideo();
        //停止视频心跳包
        mBaseHandler.removeMessages(MSG_VIDEO_HEART_BEAR_VALUE);
        AudioRecordManager.getInstance().stop();
        if (checkAliveTimer != null) {
            checkAliveTimer.cancel();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_VIDEO_HEART_BEAR_VALUE:
                VideoHeartBeatHelper.getInstance().heartBeat();
                //延迟20s之后再发送
                if (!mBaseHandler.hasMessages(MSG_VIDEO_HEART_BEAR_VALUE)) {
                    mBaseHandler.sendEmptyMessageDelayed(MSG_VIDEO_HEART_BEAR_VALUE, VideoHeartBeatHelper.DELAY);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
