package com.punuo.pet.cirlce.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.cirlce.R;
import com.punuo.pet.router.CircleRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

/**
 * Created by Kuiya on 2019/8/1.
 */
@Route(path = CircleRouter.ROUTER_ADD_ACTIVITY)
public class AddActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }
}
