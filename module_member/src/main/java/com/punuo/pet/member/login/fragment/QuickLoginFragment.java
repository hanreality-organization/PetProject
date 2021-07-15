package com.punuo.pet.member.login.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.activity.RegisterAccountActivity;
import com.punuo.pet.member.login.event.AuthEvent;
import com.punuo.pet.member.login.manager.ILoginCallBack;
import com.punuo.pet.member.login.manager.LoginManager;
import com.punuo.pet.router.CompatRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.view.CleanEditText;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class QuickLoginFragment extends BaseFragment {
    private ImageView mWechatLogin;
    private CleanEditText mEditPhone;
    private CleanEditText mEditCode;
    private TextView mGetCode;
    private TextView mLoginBtn;
    private TextView mRegisterBtn;

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
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return mFragmentView;
    }

    private void initView() {
        mWechatLogin = mFragmentView.findViewById(R.id.wechat_login);
        mEditPhone = mFragmentView.findViewById(R.id.edit_phone);
        mEditCode = mFragmentView.findViewById(R.id.edit_code);
        mGetCode = mFragmentView.findViewById(R.id.get_code);
        mLoginBtn = mFragmentView.findViewById(R.id.login_btn);
        mRegisterBtn = mFragmentView.findViewById(R.id.register_btn);
        mCount = new MyCount(60 * 1000, 1000);
        mLoginManager = new LoginManager(mActivity, mLoginCallBack);
        mPhone = MMKVUtil.getString("wsq_phone");
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
                ARouter.getInstance().build(MemberRouter.ROUTER_REGISTER_ACCOUNT_ACTIVITY)
                        .withBoolean("isRegister", true)
                        .withString("title", getString(R.string.string_register))
                        .withInt("type", RegisterAccountActivity.TYPE_INPUT_PHONE)
                        .navigation();
            }
        });
    }

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {
            ARouter.getInstance().build(CompatRouter.ROUTER_HOME_ACTIVITY).navigation();
            MMKVUtil.setString("wsq_phone", mPhone);
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

        }

        @Override
        public void setPasswordSuccess() {

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
                ToastUtils.showToast(getString(R.string.string_wx_login_not_support));
                dismissLoadingDialog();
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                ToastUtils.showToast(getString(R.string.string_wx_login_cancel));
                dismissLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            mGetCode.setText(getString(R.string.string_edit_code_timer,millisUntilFinished / 1000));
            mGetCode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            mGetCode.setEnabled(true);
            mGetCode.setText(getString(R.string.string_edit_code_again));
        }
    }
}
