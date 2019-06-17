package com.punuo.sys.sdk;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by han.chen.
 * Date on 2019/4/4.
 **/
public class PnApplication extends Application {
    private static PnApplication instance;

    public static PnApplication getInstance() {
        if (instance == null) {
            instance = new PnApplication();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
