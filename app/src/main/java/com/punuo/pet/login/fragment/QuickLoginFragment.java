package com.punuo.pet.login.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.punuo.pet.R;
import com.punuo.pet.event.AuthEvent;
import com.punuo.pet.home.HomeActivity;
import com.punuo.pet.login.ILoginCallBack;
import com.punuo.pet.login.LoginManager;
import com.punuo.pet.login.RegisterAccountActivity;
import com.punuo.pet.view.CleanEditText;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.IntentUtil;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.util.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class QuickLoginFragment extends BaseFragment {
    @Bind(R.id.wechat_login)
    ImageView mWechatLogin;
    @Bind(R.id.edit_phone)
    CleanEditText mEditPhone;
    @Bind(R.id.edit_code)
    CleanEditText mEditCode;
    @Bind(R.id.get_code)
    TextView mGetCode;
    @Bind(R.id.login_btn)
    TextView mLoginBtn;
    @Bind(R.id.register_btn)
    TextView mRegisterBtn;

    private MyCount mCount;
    private LoginManager mLoginManager;
    private BaseActivity mActivity;
    private String mPhone = "";

    public static QuickLoginFragment newInstance() {
        return new QuickLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_quick_login, container, false);
        mActivity = (BaseActivity) getActivity();
        ButterKnife.bind(this, mFragmentView);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return mFragmentView;
    }

    private void initView() {
        mCount = new MyCount(60 * 1000, 1000);
        mLoginManager = new LoginManager(mActivity, mLoginCallBack);
        mPhone = MMKVUtil.getString("wsp_phone");
        mEditPhone.setText(mPhone);
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = mEditPhone.getText().toString().trim();
                mLoginManager.sendAuthCode(mPhone);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = mEditPhone.getText().toString().trim();
                String code = mEditCode.getText().toString().trim();
                mLoginManager.loginWithPhone(mPhone, code);
            }
        });
        mWechatLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                mLoginManager.authLogin(mActivity);
            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.jumpActivity(mActivity, RegisterAccountActivity.class);
            }
        });
    }

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {
            IntentUtil.jumpActivity(mActivity, HomeActivity.class);
            MMKVUtil.setString("wsp_phone", mPhone);
            ToastUtils.showToast("登录成功");
            mActivity.finish();
        }

        @Override
        public void loginError() {

        }

        @Override
        public void getAuthCodeSuccess() {
            mCount.start();
        }

        @Override
        public void getAuthCodeError() {
            ToastUtils.showToast("发送验证码失败,请重试");
        }

        @Override
        public void setPasswordSuccess() {
            ToastUtils.showToast("注册成功");
        }

        @Override
        public void logoutSuccess() {

        }
    };
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AuthEvent authEvent) {
        if (authEvent.type == AuthEvent.TYPE_WEIXIN && authEvent.bundle != null) {
            SendAuth.Resp resp = new SendAuth.Resp(authEvent.bundle);
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                mLoginManager.loginWithWeChat(resp.code);
            } else if (resp.errCode == BaseResp.ErrCode.ERR_UNSUPPORT) {
                ToastUtils.showToast("您的微信版本还不支持第三方登录");
                dismissLoadingDialog();
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                ToastUtils.showToast("您取消了微信登录");
                dismissLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mCount != null) {
            mCount.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetCode.setText(millisUntilFinished / 1000 + "S后重发");
            mGetCode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            mGetCode.setEnabled(true);
            mGetCode.setText("重新获取");
        }
    }
}