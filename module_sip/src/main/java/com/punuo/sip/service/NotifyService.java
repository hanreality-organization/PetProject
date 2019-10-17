package com.punuo.sip.service;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.request.BaseSipRequest;

import org.zoolu.sip.message.Message;

/**
 * Created by han.chen.
 * Date on 2019-10-17.
 **/
@Route(path = ServicePath.PATH_NOTIFY)
public class NotifyService extends NormalRequestService<Object> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, Object result) {

    }

    @Override
    protected void onError(Exception e) {

    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
