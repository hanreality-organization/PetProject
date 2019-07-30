package com.punuo.pet.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

/**
 * Created by Kuiya on 2019/7/30.
 */

@Route(path= MemberRouter.ROUTER_CUETOMERSERVICE)
public class CustomerServiceActivity extends BaseSwipeBackActivity{

    private FragmentManager mFragmentManager;
    private ImageView mBack;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerservice);
        init();
    }

    public void init(){
        mBack = (ImageView)findViewById(R.id.back);
        mTitle = (TextView)findViewById(R.id.title);

        mTitle.setText("客服");

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToFinishActivity();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
