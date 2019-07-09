package com.punuo.pet.member.login.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.manager.ILoginCallBack;
import com.punuo.pet.member.login.manager.LoginManager;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 忘记密码页面
 **/
@Route(path = MemberRouter.ROUTER_FORGET_PASSWORD_ACTIVITY)
public class ForgetPasswordActivity extends BaseSwipeBackActivity {

    private TextView mTitle;
    private ImageView mBack;

    private EditText mEditPhone;
    private EditText mEditCode;
    private TextView mGetCode;
    private TextView mApply;

    private MyCount mCount;
    private LoginManager mLoginManager;

    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mLoginManager = new LoginManager(this, mLoginCallBack);
        mCount = new MyCount(60 * 1000, 1000);
        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mBack = (ImageView) findViewById(R.id.back);
        mTitle.setText("忘记密码");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });

        mEditCode = (EditText) findViewById(R.id.edit_code);
        mEditPhone = (EditText) findViewById(R.id.edit_phone);
        mGetCode = (TextView) findViewById(R.id.get_code);
        mApply = (TextView) findViewById(R.id.apply_btn);

        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = mEditPhone.getText().toString().trim();
                mLoginManager.sendAuthCode(mPhone);
            }
        });
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEditCode.getText().toString().trim();
                mLoginManager.loginWithPhone(mPhone, code);
            }
        });
    }

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {
            //跳转设置密码页面
            ARouter.getInstance().build(MemberRouter.ROUTER_REGISTER_ACCOUNT_ACTIVITY)
                    .withBoolean("isRegister", false)
                    .withString("title", "设置密码")
                    .withInt("type", RegisterAccountActivity.TYPE_SET_PWD)
                    .navigation();
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
