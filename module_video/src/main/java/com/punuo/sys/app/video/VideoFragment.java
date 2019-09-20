package com.punuo.sys.app.video;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.MediaData;
import com.punuo.sip.model.QueryData;
import com.punuo.sip.request.SipMediaRequest;
import com.punuo.sip.request.SipQueryRequest;
import com.punuo.sip.request.SipRequestListener;
import com.punuo.sys.app.video.stream.H264Config;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.zoolu.sip.message.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-09-20.
 **/
@Route(path = VideoRouter.ROUTER_VIDEO_FRAGMENT)
public class VideoFragment extends BaseFragment {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.back)
    View mBack;
    private String devId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.video_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        devId = "310023001139940001";
        return mFragmentView;
    }

    private void initView() {
        mBack.setVisibility(View.GONE);
        initTitle();
        initSubTitle();

    }

    private void initSubTitle() {
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("全屏");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 全屏播放

            }
        });
    }

    private void initTitle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        String title = simpleDateFormat.format(new Date());
        mTitle.setText(title);
    }

    private void startVideo(String devId) {
        queryMediaInfo(devId);
    }

    private void queryMediaInfo(final String devId) {
        SipQueryRequest sipQueryRequest = new SipQueryRequest(devId);
        sipQueryRequest.setSipRequestListener(new SipRequestListener<QueryData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(QueryData result, Message message) {
                if (result == null) {
                    return;
                }
                H264Config.initQueryData(result);
                inviteMedia(devId);

            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        SipUserManager.getInstance().addRequest(sipQueryRequest);
    }

    private void inviteMedia(String devId) {
        SipMediaRequest sipMediaRequest = new SipMediaRequest(devId);
        sipMediaRequest.setSipRequestListener(new SipRequestListener<MediaData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(MediaData result, Message message) {
                if (result == null) {
                    return;
                }
                H264Config.initMediaData(result);
                //TODO 开启接收视频
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        SipUserManager.getInstance().addRequest(sipMediaRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
