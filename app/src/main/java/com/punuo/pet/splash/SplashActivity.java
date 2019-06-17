package com.punuo.pet.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.punuo.pet.R;
import com.punuo.pet.account.AccountManager;
import com.punuo.pet.home.HomeActivity;
import com.punuo.pet.login.LoginActivity;
import com.punuo.pet.process.ProcessTasks;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.PreferenceUtils;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        ProcessTasks.commonLaunchTasks(PnApplication.getInstance());
        init();
    }

    private void init() {
        boolean isFirstRun = PreferenceUtils.getBoolean(this, "is_first_run", true);
        if (isFirstRun) {
            PreferenceUtils.setBoolean(this,"is_first_run", false);
            //TODO 第一次启动，可以做一些自定义的东西
        }
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AccountManager.isLoginned()) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2 * 1000);
    }
}
