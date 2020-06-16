package com.punuo.sip.service;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.H264Config;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.QueryResponse;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipMediaRequest;
import com.punuo.sip.request.SipOptionsRequest;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import org.zoolu.sip.message.Message;

/**
 * Created by han.chen.
 * Date on 2020/6/10.
 * 视频请求第一步骤返回
 **/
@Route(path = ServicePath.PATH_QUERY)
public class QueryService extends NormalRequestService<QueryResponse> {

    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, QueryResponse result) {
        H264Config.numOfTimeOut = 0;
        SipMediaRequest sipMediaRequest = new SipMediaRequest();
        SipUserManager.getInstance().addRequest(sipMediaRequest);
    }

    @Override
    protected void onError(Exception e) {
        HandlerExceptionUtils.handleException(e);
    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {
        //超时重试最多3次
        if (H264Config.numOfTimeOut < 3) {
            H264Config.numOfTimeOut++;
            SipOptionsRequest optionsRequest = new SipOptionsRequest();
            SipUserManager.getInstance().addRequest(optionsRequest);
        } else {
            ToastUtils.showToast("请求视频失败，请稍后再试");
        }
    }
}
