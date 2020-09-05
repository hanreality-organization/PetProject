package com.punuo.sip.dev;

import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2020/9/5.
 **/
public class DevManager {
    private static DevManager sInstance;
    public static DevManager getInstance() {
        if (sInstance == null) {
            synchronized (DevManager.class) {
                if (sInstance == null) {
                    sInstance = new DevManager();
                }
            }
        }
        return sInstance;
    }

    private String devId;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
       this.devId = devId;
    }

    public void refreshDevRelationShip() {
        GetDevIdRequest request = new GetDevIdRequest();
        request.addUrlParam("userName", AccountManager.getUserName());
        request.setRequestListener(new RequestListener<DevData>() {
            @Override
            public void onComplete() {
                EventBus.getDefault().post(new DevIdEvent());
            }

            @Override
            public void onSuccess(DevData result) {
                if (result == null) {
                    devId = "";
                    return;
                }
                devId = result.devId;
            }

            @Override
            public void onError(Exception e) {
                devId = "";
            }
        });
        HttpManager.addRequest(request);
    }
}
