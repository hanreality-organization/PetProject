package com.punuo.sys.app.video;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.device.model.BindDevidSuccess;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.ResetData;
import com.punuo.sip.model.VideoData;
import com.punuo.sip.model.VolumeData;
import com.punuo.sip.request.SipByeRequest;
import com.punuo.sip.request.SipControlVolumeRequest;
import com.punuo.sip.request.SipVideoRequest;
import com.punuo.sys.app.video.activity.model.deviddata;
import com.punuo.sys.app.video.activity.request.GetdevidRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
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
        TextureView.SurfaceTextureListener {

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
    @BindView(R2.id.play_music)
    View playMusic;
//    @BindView(R2.id.reset)
//    Button mReset;
    @BindView(R2.id.play_video)
    Button mplayvideo;
    @BindView(R2.id.add_voice)
    View add_voice;
    @BindView(R2.id.down_voice)
    View down_voice;

    private MediaPlayer mMediaPlayer;
    private Surface surface;
    private String devId;
    private MyAsyncTask mAsyncTask;
    private BaseHandler mBaseHandler;
    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.video_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        mBaseHandler = new BaseHandler(this);
        //Toast.makeText(getActivity(),"请先确认已绑定设备再获取视频",Toast.LENGTH_LONG).show();
        getdevid();
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
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();
                showLoadingDialogWithCancel("正在获取视频...", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStop.performClick();
                        dismissLoadingDialog();
                    }
                });
                startVideo(devId);
            }
        });
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();
                releaseMediaPlayer();
                closeVideo();
                isPlaying = false;
                mPlay.setEnabled(true);
            }
        });
//        mReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SipResetRequest sipResetRequest=new SipResetRequest();
//                SipUserManager.getInstance().addRequest(sipResetRequest);
//            }
//        });
        playMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(VideoRouter.ROUTER_MUSIC_CHOOSE_ACTIVITY)
                        .navigation();
            }
        });
        mplayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_CHOOSE_ACTIVITY)
//                        .navigation();
               showDialog();
            }
        });
        down_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SipControlVolumeRequest sipControlVolumeRequest=new SipControlVolumeRequest("lower");
                SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
            }
        });
        add_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SipControlVolumeRequest sipControlVolumeRequest=new SipControlVolumeRequest("raise");
                SipUserManager.getInstance().addRequest(sipControlVolumeRequest);
            }
        });
    }

    private void initTextureView() {
        int width = CommonUtil.getWidth();
        mTextureView.getLayoutParams().height = H264Config.VIDEO_HEIGHT * width / H264Config.VIDEO_WIDTH;//H264Config.VIDEO_HEIGHT
        mTextureView.setRotation(90);
        mTextureView.setScaleX((float) H264Config.VIDEO_HEIGHT / H264Config.VIDEO_WIDTH);
        mTextureView.setScaleY((float) H264Config.VIDEO_WIDTH / H264Config.VIDEO_HEIGHT);
        mTextureView.setSurfaceTextureListener(this);
    }

    private void initSubTitle() {
        mSubTitle.setVisibility(View.GONE);
        mSubTitle.setText("全屏");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 全屏播放
            }
        });
    }

    private void startVideo(String devId) {
        requestVideo(devId);
    }

    private void initTitle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        String title = simpleDateFormat.format(new Date());
        mTitle.setText(title);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoData event) {
        mAsyncTask = new MyAsyncTask(getActivity());
        mAsyncTask.execute();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ResetData result) {
        Toast.makeText(getActivity(),"重置成功",Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindDevidSuccess result){
        getdevid();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VolumeData result){
        if(TextUtils.equals(result.volume,"raise")){
            Toast.makeText(getActivity(),"音量加",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.equals(result.volume,"lower")){
            Toast.makeText(getActivity(),"音量减",Toast.LENGTH_SHORT).show();
        }
    }
    private GetdevidRequest mGetdevidRequest;
    public void getdevid(){
        if (mGetdevidRequest!= null && !mGetdevidRequest.isFinish()) {
            return;
        }
        mGetdevidRequest=new GetdevidRequest();
        mGetdevidRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetdevidRequest.setRequestListener(new RequestListener<deviddata>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(deviddata result) {
                if (result == null) {
                    return;
                }
                devId=result.devid;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetdevidRequest);
    }

    private void closeVideo() {
        SipByeRequest sipByeRequest = new SipByeRequest(devId);
        SipUserManager.getInstance().addRequest(sipByeRequest);
    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(getActivity(), true);
            mMediaPlayer.setDataSource(H264Config.RTMP_STREAM);
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
        isPlaying = true;
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
                mPlay.setEnabled(false);
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

    public void showDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        TextView title = new TextView(getContext());
        title.setText("视频侦测");
        title.setPadding(10,10,10,10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        dialog.setCustomTitle(title);
        TextView msg = new TextView(getContext());
        msg.setText("\n"+"敬请期待!"+"\n");
        msg.setPadding(10, 10, 10, 10);
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(18);
        dialog.setView(msg);
        dialog.show();
    }
}
