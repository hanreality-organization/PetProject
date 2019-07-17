package com.punuo.pet.member.login.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.manager.ILoginCallBack;
import com.punuo.pet.member.login.manager.LoginManager;
import com.punuo.pet.member.login.request.BindPhoneRequest;
import com.punuo.pet.router.CompatRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.ToastUtils;

/**
 * Created by han.chen.
 * Date on 2019-07-01.
 **/
@Route(path = MemberRouter.ROUTER_BIND_PHONE_ACTIVITY)
public class BindPhoneActivity extends BaseActivity {
    private EditText mEditPhone;
    private EditText mEditCode;
    private TextView mGetCode;
    private TextView mBindBtn;
    private String mPhone;
    @Autowired(name = "openId")
    String openId;
    private MyCount mCount;
    private LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_layout);
        ARouter.getInstance().inject(this);
        mLoginManager = new LoginManager(this, mLoginCallBack);
        mCount = new MyCount(60 * 1000, 1000);
        initView();
    }

    private void initView() {
        TextView title = findViewById(R.id.title);
        title.setText("绑定手机");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mEditCode = findViewById(R.id.edit_code);
        mEditPhone = findViewById(R.id.edit_phone);
        mGetCode = findViewById(R.id.get_code);
        mBindBtn = findViewById(R.id.bind_btn);
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = mEditPhone.getText().toString().trim();
                mLoginManager.sendAuthCode(mPhone);
            }
        });
        mBindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEditCode.getText().toString().trim();
                bindPhone(code);
            }
        });
    }

    private BindPhoneRequest mBindPhoneRequest;
    private void bindPhone(String code) {
        if (mBindPhoneRequest != null && !mBindPhoneRequest.isFinish()) {
            return;
        }
        mBindPhoneRequest = new BindPhoneRequest();
        mBindPhoneRequest.addUrlParam("code", code);
        mBindPhoneRequest.addUrlParam("userName", mPhone);
        mBindPhoneRequest.addUrlParam("openId", openId);
        mBindPhoneRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
                if (result.success) {
                    ARouter.getInstance().build(CompatRouter.ROUTER_HOME_ACTIVITY)
                            .navigation();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mBindPhoneRequest);
    }

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {

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

    @Override
    public void onBackPressed() {
        ToastUtils.showToast("为了更好的使用软件功能，需要绑定手机");
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
