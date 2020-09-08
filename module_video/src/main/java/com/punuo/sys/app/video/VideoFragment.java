package com.punuo.sys.app.video;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.dev.DevIdEvent;
import com.punuo.sip.dev.DevManager;
import com.punuo.sip.model.ResetData;
import com.punuo.sip.model.VideoData;
import com.punuo.sip.model.VolumeData;
import com.punuo.sip.request.SipByeRequest;
import com.punuo.sip.request.SipControlVolumeRequest;
import com.punuo.sip.request.SipVideoRequest;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.BaseHandler;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import org.easydarwin.video.EasyPlayerClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_FRAGMENT)
public class VideoFragment extends BaseFragment implements BaseHandler.MessageHandler {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.back)
    View mBack;
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
    @BindView(R2.id.content_view)
    View contentView;
    @BindView(R2.id.dev_id_display)
    TextView mDevIdDisplay;
    @BindView(R2.id.surface)
    TextureView mSurface;

    private boolean isPlaying = false;
    private EasyPlayerClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.video_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        //Toast.makeText(getActivity(),"请先确认已绑定设备再获取视频",Toast.LENGTH_LONG).show();
        DevManager.getInstance().refreshDevRelationShip();
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
                if (!checkDevId()) {
                    return;
                }
                if (!isPlaying) {
                    showLoadingDialogWithCancel("正在获取视频...", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissLoadingDialog();
                            stopVideo();
                        }
                    });
                    startVideo(DevManager.getInstance().getDevId());
                }
            }
        });
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkDevId()) {
                    return;
                }
                ARouter.getInstance().build(VideoRouter.ROUTER_MUSIC_CHOOSE_ACTIVITY)
                        .navigation();
            }
        });
        mPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkDevId()) {
                    return;
                }
                ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_CHOOSE_ACTIVITY)
                        .navigation();
            }
        });
        down_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkDevId()) {
                    return;
                }
                SipControlVolumeRequest sipControlVolumeRequest = new SipControlVolumeRequest("lower");
                SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
            }
        });
        add_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkDevId()) {
                    return;
                }
                SipControlVolumeRequest sipControlVolumeRequest = new SipControlVolumeRequest("raise");
                SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
            }
        });

        client = new EasyPlayerClient(getContext(),BuildConfig.PLAYER_RTMP_KEY, mSurface, null, null);
    }

    private void stopVideo() {
        closeVideo();
//        player.stop();
//        player.hideAll();
//        player.release();
        client.stop();
        isPlaying = false;
        mSubTitle.setEnabled(false);
        mPlayStatus.setVisibility(View.VISIBLE);
    }

    private void initTextureView() {
        int width = CommonUtil.getWidth();
        mSurface.getLayoutParams().height = H264Config.VIDEO_HEIGHT * width / H264Config.VIDEO_WIDTH;
    }

    private void initSubTitle() {
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("停止播放");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    closeVideo();
//                    player.stop();
//                    player.hideAll();
//                    player.release();
                    client.stop();
                    isPlaying = false;
                    mPlayStatus.setVisibility(View.VISIBLE);
                    mSubTitle.setEnabled(false);
                }
            }
        });
        mSubTitle.setEnabled(false);
    }

    private void startVideo(String devId) {
        requestVideo(devId);
    }

    private void initTitle() {
        mTitle.setText("视频");
    }


    private void requestVideo(final String devId) {
        SipVideoRequest sipVideoRequest = new SipVideoRequest(devId);
        SipUserManager.getInstance().addRequest(sipVideoRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && isPlaying) {
            stopVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isPlaying) {
            stopVideo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoData event) {
        client.play(event.mVideoUrl);
        isPlaying = true;
        mSubTitle.setEnabled(true);
        dismissLoadingDialog();
        mPlayStatus.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DevIdEvent event) {
        mDevIdDisplay.setText(DevManager.getInstance().getDevId());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResetData result) {
        Toast.makeText(getActivity(), "开启成功", Toast.LENGTH_SHORT).show();
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

    private void closeVideo() {
        if (checkDevId()) {
            SipByeRequest sipByeRequest = new SipByeRequest(DevManager.getInstance().getDevId());
            SipUserManager.getInstance().addRequest(sipByeRequest);
        }
    }

    @Override
    public void handleMessage(Message msg) {
    }

    /**
     * 检测是否绑定设备
     *
     * @return
     */
    private boolean checkDevId() {
        if (!TextUtils.isEmpty(DevManager.getInstance().getDevId())) {
            return true;
        } else {
            ToastUtils.showToast("请先确认已绑定设备");
            ARouter.getInstance().build(HomeRouter.ROUTER_BIND_DEVICE_ACTIVITY)
                    .navigation();
            return false;
        }
    }
}
