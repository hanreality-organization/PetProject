package com.punuo.pet.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.R2;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.sip.dev.DevManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kuiya on 2019/7/30.
 */

@Route(path = MemberRouter.ROUTER_ABOUT_ACTIVITY)
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R2.id.user_agreement)
    RelativeLayout userAgreement;
    private ImageView mBack;
    private TextView mTitle;
    private TextView version;
    private ImageView mLogo;
    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        init();

    }

    public void init() {
        mLogo = findViewById(R.id.logo);
        mBack = findViewById(R.id.back);
        mTitle = findViewById(R.id.title);
        version = findViewById(R.id.version);

        mTitle.setText("关于我们");
        version.setText("当前版本：v" + DeviceHelper.getVersionName());
        mBack.setOnClickListener(this);
        userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(SDKRouter.ROUTER_WEB_VIEW_ACTIVITY)
                        .withString("url", "https://pet.qinqingonline.com/static/pet_user_protocol.html")
                        .withString("title", "用户协议")
                        .navigation();
            }
        });
        ViewGroup.LayoutParams layoutParams = mLogo.getLayoutParams();
        int width = CommonUtil.getWidth() - CommonUtil.dip2px(120f);
        layoutParams.width = width;
        layoutParams.height = width * 3 / 4;
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount == 3) {
                    DevManager.getInstance().clearDevConfirm(AboutActivity.this, 1);
                    clickCount = 0;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        }
    }
}
