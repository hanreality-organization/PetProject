package com.punuo.sip.service;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.H264Config;
import com.punuo.sip.model.MediaData;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;

import org.zoolu.sip.message.Message;

/**
 * Created by han.chen.
 * Date on 2019-08-21.
 **/
@Route(path = ServicePath.PATH_MEDIA)
public class MediaService extends NormalRequestService<MediaData> {

    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, MediaData result) {
        Log.d("han.chen", "Media 收到视频请求");
        if (result != null) {
            H264Config.initMediaData(result);
            //TODO 开启接收视频
            ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
                    .navigation();
        }
    }

    @Override
    protected void onError(Exception e) {
        HandlerExceptionUtils.handleException(e);
    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
