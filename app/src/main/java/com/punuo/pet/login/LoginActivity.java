package com.punuo.pet.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.punuo.pet.R;
import com.punuo.pet.home.HomeActivity;
import com.punuo.pet.login.fragment.NormalLoginFragment;
import com.punuo.pet.login.fragment.QuickLoginFragment;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class LoginActivity extends BaseActivity {
    public static final int TYPE_QUICK_LOGIN = 1;
    public static final int TYPE_NORMAL_LOGIN = 2;
    @Bind(R.id.quick_login)
    TextView mQuickLogin;
    @Bind(R.id.normal_login)
    TextView mNormalLogin;
    @Bind(R.id.logo)
    ImageView mLogo;
    @Bind(R.id.id_content)
    FrameLayout mContent;
    @Bind(R.id.content_container)
    LinearLayout mContentContainer;
    @Bind(R.id.cache_bg)
    View mCacheBg;

    private LoginManager mLoginManager;
    private int TRANSLATION_Y = 0;
    private QuickLoginFragment mQuickLoginFragment;
    private NormalLoginFragment mNormalLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);
        mLoginManager = new LoginManager(this, mLoginCallBack);

        initView();
    }

    private void initView() {
        int screenHeight = CommonUtil.getHeight();
        int top = CommonUtil.dip2px(150f);
        int tabHeight = CommonUtil.dip2px(64f);
        int statusHeight = StatusBarUtil.getStatusBarHeight(this);
        TRANSLATION_Y = screenHeight - top - tabHeight - statusHeight;
        mContentContainer.setTranslationY(TRANSLATION_Y);
        mNormalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentContainer.animate().translationY(0).setInterpolator(new LinearInterpolator()).setDuration(300);
                switchFragment(TYPE_NORMAL_LOGIN);
            }
        });
        mQuickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentContainer.animate().translationY(0).setInterpolator(new LinearInterpolator()).setDuration(300);
                switchFragment(TYPE_QUICK_LOGIN);
            }
        });

        mCacheBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentContainer.animate().translationY(TRANSLATION_Y).setInterpolator(new LinearInterpolator()).setDuration(300);

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

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {
            jumpToHome();
        }

        @Override
        public void loginError() {

        }
    };

    private void jumpToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
