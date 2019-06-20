package com.punuo.pet.login.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punuo.pet.R;
import com.punuo.pet.home.HomeActivity;
import com.punuo.pet.login.ILoginCallBack;
import com.punuo.pet.login.LoginManager;
import com.punuo.pet.view.CleanEditText;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.IntentUtil;
import com.punuo.sys.sdk.util.MMKVUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class NormalLoginFragment extends BaseFragment {

    @Bind(R.id.edit_account)
    CleanEditText mEditAccount;
    @Bind(R.id.edit_password)
    CleanEditText mEditPassword;
    @Bind(R.id.login_btn)
    TextView mLoginBtn;
    @Bind(R.id.forget_password)
    TextView mForgetPassword;

    private View mView;
    private BaseActivity mActivity;
    private String mAccount;

    private LoginManager mLoginManager;

    public static NormalLoginFragment newInstance() {
        return new NormalLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mView = inflater.inflate(R.layout.fragment_normal_login, container, false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        mAccount = MMKVUtil.getString("wsq_account");
        mEditAccount.setText(mAccount);
        mLoginManager = new LoginManager(mActivity, mLoginCallBack);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccount = mEditAccount.getText().toString().trim();
                String pwd = mEditPassword.getText().toString();
                mLoginManager.loginWithAccount(mAccount, pwd);
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {
            IntentUtil.jumpActivity(mActivity, HomeActivity.class);
            MMKVUtil.setString("wsq_account", mAccount);
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
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
