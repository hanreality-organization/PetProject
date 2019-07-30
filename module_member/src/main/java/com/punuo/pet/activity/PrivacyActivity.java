package com.punuo.pet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

/**
 * Created by Kuiya on 2019/7/30.
 */
@Route(path = MemberRouter.ROUTER_PRIVACY_ACTIVITY)
public class PrivacyActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        init();
    }

    public void init(){
        mBack = (ImageView)findViewById(R.id.back);

        mBack.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.back){
            scrollToFinishActivity();
        }
    }
}
