package com.punuo.pet.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;

/**
 * Created by Kuiya on 2019/7/29.
 */
@Route(path = MemberRouter.ROUTER_CHANGE_AVATER_ACTIVITY)
public class ChangeAvaterActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
