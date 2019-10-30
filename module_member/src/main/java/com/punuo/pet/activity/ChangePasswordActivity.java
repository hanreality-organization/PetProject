package com.punuo.pet.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;

/**
 * Created by Kuiya on 2019/7/29.
 */
@Route(path = MemberRouter.ROUTER_CHANGE_PASSWORD_ACTIVITY)
public class ChangePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
