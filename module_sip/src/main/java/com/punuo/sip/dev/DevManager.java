package com.punuo.sip.dev;

import android.text.TextUtils;

import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipOnLineRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;

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

    public boolean isBindDevice() {
        return !TextUtils.isEmpty(devId);
    }

    public void refreshDevRelationShip() {
        final GetDevIdRequest request = new GetDevIdRequest();
        request.addUrlParam("userName", AccountManager.getUserName());
        request.setRequestListener(new RequestListener<DevData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(DevData result) {
                if (result == null) {
                    devId = "";
                    return;
                }
                if (!TextUtils.isEmpty(result.devId)) {
                    devId = result.devId;
                } else  {
                    devId = "";
                }
            }

            @Override
            public void onError(Exception e) {
                devId = "";
            }
        });
        HttpManager.addRequest(request);
    }

    /**
     * 获取设备是否在线
     */
    public void isOnline() {
        SipOnLineRequest sipOnLineRequest = new SipOnLineRequest();
        SipUserManager.getInstance().addRequest(sipOnLineRequest);
    }
}
