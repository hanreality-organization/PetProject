package com.punuo.pet.member.login.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.fragment.NormalLoginFragment;
import com.punuo.pet.member.login.fragment.QuickLoginFragment;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
@Route(path = MemberRouter.ROUTER_LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity {
    public static final int TYPE_QUICK_LOGIN = 1;
    public static final int TYPE_NORMAL_LOGIN = 2;
    private TextView mQuickLogin;
    private TextView mNormalLogin;
    private ImageView mLogo;
    private FrameLayout mContent;
    private LinearLayout mContentContainer;
    private View mCacheBg;

    private int TRANSLATION_Y = 0;
    private QuickLoginFragment mQuickLoginFragment;
    private NormalLoginFragment mNormalLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, true);
        View mStatusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
    }

    private void initView() {
        mQuickLogin = findViewById(R.id.quick_login);
        mNormalLogin = findViewById(R.id.normal_login);
        mLogo = findViewById(R.id.logo);
        mContent = findViewById(R.id.id_content);
        mContentContainer = findViewById(R.id.content_container);
        mCacheBg = findViewById(R.id.cache_bg);

        int screenHeight = CommonUtil.getHeight();
        int top = CommonUtil.dip2px(150f);
        int tabHeight = CommonUtil.dip2px(44f);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        TRANSLATION_Y = screenHeight - top - tabHeight - statusHeight;
        mContentContainer.setTranslationY(TRANSLATION_Y);
        mNormalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentContainer.animate().translationY(0)
                        .setInterpolator(new LinearInterpolator()).setDuration(300);
                mNormalLogin.setTextColor(Color.parseColor("#333333"));
                mQuickLogin.setTextColor(Color.parseColor("#FFFFFF"));
                switchFragment(TYPE_NORMAL_LOGIN);
            }
        });
        mQuickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentContainer.animate().translationY(0)
                        .setInterpolator(new LinearInterpolator()).setDuration(300);
                mQuickLogin.setTextColor(Color.parseColor("#333333"));
                mNormalLogin.setTextColor(Color.parseColor("#FFFFFF"));
                switchFragment(TYPE_QUICK_LOGIN);
            }
        });

        mCacheBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentContainer.animate().translationY(TRANSLATION_Y)
                        .setInterpolator(new LinearInterpolator()).setDuration(300);
                mQuickLogin.setTextColor(Color.parseColor("#FFFFFF"));
                mNormalLogin.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });
    }

    private void switchFragment(int type) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.id_content);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (type) {
            case TYPE_QUICK_LOGIN:
                if (fragment instanceof QuickLoginFragment) {
                    return;
                }
                if (mQuickLoginFragment == null) {
                    mQuickLoginFragment = QuickLoginFragment.newInstance();
                    mQuickLoginFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.id_content, mQuickLoginFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case TYPE_NORMAL_LOGIN:
                if (fragment instanceof NormalLoginFragment) {
                    return;
                }
                if (mNormalLoginFragment == null) {
                    mNormalLoginFragment = NormalLoginFragment.newInstance();
                    mNormalLoginFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.id_content, mNormalLoginFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
