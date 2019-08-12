package com.punuo.sip;

import com.punuo.sip.event.ReRegisterEvent;
import com.punuo.sip.model.HeartBeatData;
import com.punuo.sip.request.SipHeartBeatRequest;
import com.punuo.sip.request.SipRequestListener;
import com.punuo.sys.sdk.account.AccountManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 * 心跳包活工具
 **/
public class HeartBeatHelper {
    public static final int DELAY = 20 * 1000;

    public static void heartBeat() {
        if (!AccountManager.isLoginned()) {
            return;
        }
        SipHeartBeatRequest heartBeatRequest = new SipHeartBeatRequest();
        heartBeatRequest.setSipRequestListener(new SipRequestListener<HeartBeatData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(HeartBeatData result) {
                if (result == null) {
                    return;
                }
                if (result.mLoginResponse == null) {
                    //掉线了， 重新注册
                    EventBus.getDefault().post(new ReRegisterEvent());
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        SipUserManager.getInstance().addRequest(heartBeatRequest);
    }
}
