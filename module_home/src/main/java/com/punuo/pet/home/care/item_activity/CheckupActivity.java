package com.punuo.pet.home.care.item_activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

@Route(path = HomeRouter.ROUTER_CARE_CHECKUP_ACTIVITY)
public class CheckupActivity extends BaseSwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_checkup);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
