package com.punuo.pet.compat.process;

import android.app.Application;

import com.punuo.pet.compat.interceptor.PNInterceptor;
import com.punuo.sip.ISipConfig;
import com.punuo.sip.SipConfig;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.thread.SipInitThread;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.ActivityLifeCycle;
import com.punuo.sys.sdk.httplib.HttpConfig;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.IHttpConfig;
import com.punuo.sys.sdk.util.DebugCrashHandler;
import com.punuo.sys.sdk.util.DeviceHelper;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class ProcessTasks {

    public static void commonLaunchTasks(Application app) {
        if (DeviceHelper.isApkInDebug()) {
            DebugCrashHandler.getInstance().init(); //崩溃日志收集
        }
        app.registerActivityLifecycleCallbacks(ActivityLifeCycle.getInstance());
        HttpConfig.init(new IHttpConfig() {
            @Override
            public String getHost() {
                return "pet.qinqingonline.com";
            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public boolean isUseHttps() {
                return false;
            }

            @Override
            public String getUserAgent() {
                return "punuo";
            }

            @Override
            public String getPrefixPath() {
                return "";
            }
        });
        HttpManager.setDebug(true);
        HttpManager.addInterceptor(new PNInterceptor());
        HttpManager.init();
        SipConfig.init(new ISipConfig() {
            NameAddress mServerAddress;
            NameAddress mUserNormalAddress;
            @Override
            public String getServerIp() {
                return "pet.qinqingonline.com";//39.98.36.250
            }

            @Override
            public int getPort() {
                return 6061;
            }

            @Override
            public NameAddress getServerAddress() {
                if (mServerAddress == null) {
                    SipURL remote = new SipURL(SipConfig.SERVER_ID, SipConfig.getServerIp(), SipConfig.getPort());
                    mServerAddress = new NameAddress(SipConfig.SERVER_NAME, remote);
                }
                return mServerAddress;
            }

            @Override
            public NameAddress getUserRegisterAddress() {
                    SipURL local = new SipURL(SipConfig.REGISTER_ID, SipConfig.getServerIp(), SipConfig.getPort());
                return new NameAddress(AccountManager.getUserName(), local);
            }

            @Override
            public NameAddress getUserNormalAddress() {
                if (mUserNormalAddress == null) {
                    SipURL local = new SipURL(AccountManager.getUserInfo().userId, SipConfig.getServerIp(), SipConfig.getPort());
                    mUserNormalAddress = new NameAddress(AccountManager.getUserName(), local);
                }
                return mUserNormalAddress;
            }

            @Override
            public void reset() {
                mServerAddress = null;
                mUserNormalAddress = null;
            }
        });
        SipUserManager.setContext(app);
        new SipInitThread().start();

    }
}
