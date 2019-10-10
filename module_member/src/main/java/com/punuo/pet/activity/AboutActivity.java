package com.punuo.pet.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.StatusBarUtil;

/**
 * Created by Kuiya on 2019/7/30.
 */

@Route(path = MemberRouter.ROUTER_ABOUT_ACTIVITY)
public class AboutActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    private ImageView mBack;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();

    }

    public void init(){

        mBack = (ImageView)findViewById(R.id.back);
        mTitle = (TextView)findViewById(R.id.title);

        mTitle.setText("关于我们");
        mBack.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.back){
            scrollToFinishActivity();
        }
    }
}
