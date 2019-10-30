package com.punuo.pet.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

@Route(path = MemberRouter.ROUTER_SYSTEM_NEWS_ACTIVITY)
public class SystemNewsActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemnews);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
