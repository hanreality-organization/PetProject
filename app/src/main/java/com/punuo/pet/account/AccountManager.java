package com.punuo.pet.account;

import android.content.Context;
import android.text.TextUtils;

import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.httplib.JsonUtil;
import com.punuo.sys.sdk.util.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class AccountManager {

    private static boolean sIsLogined = false;
    private static String sSession = "";
    private static UserInfo sUserInfo = null;


    public static boolean isLoginned() {
        if (!sIsLogined || TextUtils.isEmpty(sSession)) {
            Context context = PnApplication.getInstance();
            sSession = PreferenceUtils.getString(context, "wsq_pref_session");
            sIsLogined = !TextUtils.equals(sSession, "");
        }
        return sIsLogined;
    }

    public static void setUserInfo(UserInfo userInfo) {
        Context context = PnApplication.getInstance();
        if (userInfo != null) {
            sUserInfo = userInfo;
            PreferenceUtils.setString(context, "wsq_pref_user", JsonUtil.toJson(userInfo));
            //重新设置本地用户信息时候会发出通知具体页面可以接收做相应的处理
            EventBus.getDefault().post(userInfo);
        }
    }

    public static UserInfo getUserInfo() {
        if (sUserInfo == null) {
            Context context = PnApplication.getInstance();
            sUserInfo = (UserInfo) JsonUtil.fromJson(PreferenceUtils.getString(context, "wsq_pref_user"), UserInfo.class);
            sUserInfo = sUserInfo == null ? new UserInfo() : sUserInfo;
        }

        return sUserInfo;
    }

    public static void setSession(String session) {
        sSession = session;
        PreferenceUtils.setString(PnApplication.getInstance(), "wsq_pref_session", session);
    }

    public static String getSession() {
        if (TextUtils.isEmpty(sSession)) {
            sSession = PreferenceUtils.getString(PnApplication.getInstance(), "wsq_pref_session");
        }
        return sSession;
    }


    public static void clearAccountData() {
        sIsLogined = false;
        sSession = "";
        sUserInfo = null;
        PreferenceUtils.removeData(PnApplication.getInstance(), "wsq_pref_session");
        PreferenceUtils.removeData(PnApplication.getInstance(), "wsq_pref_user");
    }
}
