package com.punuo.sip.service;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.event.VideoFailedEvent;
import com.punuo.sip.model.MediaData;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipMediaRequest;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.zoolu.sip.message.Message;

/**
 * Created by han.chen.
 * Date on 2020/6/10.
 * 视频请求第二步骤返回
 **/
@Route(path = ServicePath.PATH_MEDIA)
public class MediaService extends NormalRequestService<MediaData> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, MediaData result) {
        //初始化视频通道数据
        H264Config.initMediaData(result);
        //TODO 接收视频流
        EventBus.getDefault().post(result);
    }

    @Override
    protected void onError(Exception e) {
        HandlerExceptionUtils.handleException(e);
    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {
        if (H264Config.numOfTimeOut < 3) {
            H264Config.numOfTimeOut++;
            SipMediaRequest sipMediaRequest = new SipMediaRequest();
            SipUserManager.getInstance().addRequest(sipMediaRequest);
        } else {
            EventBus.getDefault().post(new VideoFailedEvent());
        }
    }
}
