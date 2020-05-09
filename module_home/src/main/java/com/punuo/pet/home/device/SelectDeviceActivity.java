package com.punuo.pet.home.device;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

/**
 * Created by han.chen.
 * Date on 2020/5/4.
 **/
@Route(path = HomeRouter.ROUTER_SELECT_DEVICE_ACTIVITY)
public class SelectDeviceActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_select_device_activity);
    }
}
