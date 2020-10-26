package com.punuo.sys.app.video;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hengyi.fastvideoplayer.library.FastVideoPlayer;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.dev.BindDevSuccessEvent;
import com.punuo.sip.dev.DevManager;
import com.punuo.sip.dev.UnBindDevSuccessEvent;
import com.punuo.sip.model.DevNotifyData;
import com.punuo.sip.model.LoginResponse;
import com.punuo.sip.model.OnLineData;
import com.punuo.sip.model.ResetData;
import com.punuo.sip.model.VideoData;
import com.punuo.sip.model.VolumeData;
import com.punuo.sip.request.SipByeRequest;
import com.punuo.sip.request.SipControlVolumeRequest;
import com.punuo.sip.request.SipVideoRequest;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.BaseHandler;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.view.BreatheView;

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
    @BindView(R2.id.fast_video_play)
    FastVideoPlayer player;
    @BindView(R2.id.content_view)
    View contentView;

    @BindView(R2.id.breathView)
    BreatheView mBreatheView;
    @BindView(R2.id.device_status)
    View deviceStatus;
    @BindView(R2.id.wifistate)
    TextView mWifiState;
    @BindView(R2.id.status_bar)
    View mStatusBar;

    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.video_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        DevManager.getInstance().refreshDevRelationShip();
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        initView();
        DevManager.getInstance().isOnline();
        return mFragmentView;
    }

    private void initView() {
        mBack.setVisibility(View.GONE);
        initTitle();
        initSubTitle();
        initTextureView();
        mPlayStatus.setOnClickListener(v -> {
            if (!checkDevId()) {
                return;
            }
            if (!isPlaying) {
                showLoadingDialogWithCancel("正在获取视频...", v1 -> {
                    dismissLoadingDialog();
                    stopVideo();
                });
                startVideo(DevManager.getInstance().getDevId());
            }
        });
        playMusic.setOnClickListener(v -> {
            if (!checkDevId()) {
                return;
            }
            ARouter.getInstance().build(VideoRouter.ROUTER_MUSIC_CHOOSE_ACTIVITY)
                    .navigation();
        });
        mPlayVideo.setOnClickListener(view -> {
            if (!checkDevId()) {
                return;
            }
            ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_CHOOSE_ACTIVITY)
                    .navigation();
        });
        down_voice.setOnClickListener(view -> {
            if (!checkDevId()) {
                return;
            }
            SipControlVolumeRequest sipControlVolumeRequest = new SipControlVolumeRequest("lower");
            SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
        });
        add_voice.setOnClickListener(view -> {
            if (!checkDevId()) {
                return;
            }
            SipControlVolumeRequest sipControlVolumeRequest = new SipControlVolumeRequest("raise");
            SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
        });
        deviceStatus.setOnClickListener(v -> {
            ARouter.getInstance().build(HomeRouter.ROUTER_DEVICE_MANAGER_ACTIVITY).navigation();
        });
        layer();
        mBreatheView.setInterval(2000)
                .setCoreRadius(7f)
                .setDiffusMaxWidth(10f)
                .setDiffusColor(Color.parseColor("#ff1940"))
                .setCoreColor(Color.parseColor("#ff1940"))
                .onStart();
    }

    private void layer() {
        if (!MMKVUtil.getBoolean("deviceManageGuide", false)) {
            View layer = LayoutInflater.from(getActivity()).inflate(R.layout.device_gudie_layout, null);
            layer.setPadding(0, mStatusBar.getLayoutParams().height, 0, 0);
            if (getActivity() != null) {
                getActivity().addContentView(layer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                MMKVUtil.setBoolean("deviceManageGuide", true);
                layer.setOnClickListener(v -> {
                    if (v.getParent() instanceof ViewGroup) {
                        ((ViewGroup) v.getParent()).removeView(v);
                    }
                });
            }
        }
    }

    private void stopVideo() {
        closeVideo();
        player.stop();
        player.hideAll();
        player.release();
        isPlaying = false;
        mSubTitle.setEnabled(false);
        mPlayStatus.setVisibility(View.VISIBLE);
    }

    private void initTextureView() {
        player.setLive(true);//是直播还是点播  false为点播
        player.setScaleType(FastVideoPlayer.SCALETYPE_FITXY);
        player.setTitle("梦视喂食器");
        player.setShowNavIcon(false);
        player.setHideControl(true);
        int width = CommonUtil.getWidth();
        player.getLayoutParams().height = H264Config.VIDEO_HEIGHT * width / H264Config.VIDEO_WIDTH;//H264Config.VIDEO_HEIGHT
    }

    private void initSubTitle() {
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("停止播放");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    closeVideo();
                    player.stop();
                    player.hideAll();
                    player.release();
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
        if (player != null) {
            player.onDestroy();
        }
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
        player.setUrl(event.mVideoUrl);
        player.play();
        isPlaying = true;
        mSubTitle.setEnabled(true);
        dismissLoadingDialog();
        mPlayStatus.setVisibility(View.GONE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResetData result) {
        ToastUtils.showToast("开启成功");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VolumeData result) {
        if (TextUtils.equals(result.volume, "raise")) {
            ToastUtils.showToast("音量加");
        }
        if (TextUtils.equals(result.volume, "lower")) {
            ToastUtils.showToast("音量减");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DevNotifyData result) {
        changeDeviceStatus(result.mDevInfo.live);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnLineData result) {
        int live = Integer.parseInt(result.live);
        changeDeviceStatus(live);
    }

    private void changeDeviceStatus(int live) {
        if (live == 1) {
            mWifiState.setText("在线");
            mBreatheView.setCoreColor(Color.parseColor("#8BC34A"));
            mBreatheView.setDiffusColor(Color.parseColor("#8BC34A"));
        } else {
            String text = TextUtils.isEmpty(DevManager.getInstance().getDevId()) ? "未绑定" : "离线";
            mWifiState.setText(text);
            mBreatheView.setCoreColor(Color.parseColor("#ff0000"));
            mBreatheView.setDiffusColor(Color.parseColor("#ff0000"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginResponse event) {
        int live = Integer.parseInt(event.live);
        changeDeviceStatus(live);
    }

    /**
     * 绑定成功 需要重新获取设备在线信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindDevSuccessEvent event) {
        DevManager.getInstance().isOnline();
    }

    /**
     * 解绑之后 要重置设备在线信息为 离线
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnBindDevSuccessEvent event) {
        changeDeviceStatus(0);
    }
}
