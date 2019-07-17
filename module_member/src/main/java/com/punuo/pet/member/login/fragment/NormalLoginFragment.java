package com.punuo.pet.member.login.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.manager.ILoginCallBack;
import com.punuo.pet.member.login.manager.LoginManager;
import com.punuo.pet.router.CompatRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.view.CleanEditText;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class NormalLoginFragment extends BaseFragment {

    private CleanEditText mEditAccount;
    private CleanEditText mEditPassword;
    private TextView mLoginBtn;
    private TextView mForgetPassword;

    private BaseActivity mActivity;
    private String mPhone;

    private LoginManager mLoginManager;

    public static NormalLoginFragment newInstance() {
        return new NormalLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mFragmentView = inflater.inflate(R.layout.fragment_normal_login, container, false);
        initView();
        return mFragmentView;
    }

    private void initView() {
        mEditAccount = mFragmentView.findViewById(R.id.edit_account);
        mEditPassword = mFragmentView.findViewById(R.id.edit_password);
        mLoginBtn = mFragmentView.findViewById(R.id.login_btn);
        mForgetPassword = mFragmentView.findViewById(R.id.forget_password);
        mPhone = MMKVUtil.getString("wsq_phone");
        mEditAccount.setText(mPhone);
        mLoginManager = new LoginManager(mActivity, mLoginCallBack);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = mEditAccount.getText().toString().trim();
                String pwd = mEditPassword.getText().toString();
                mLoginManager.loginWithAccount(mPhone, pwd);
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_FORGET_PASSWORD_ACTIVITY).navigation();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
