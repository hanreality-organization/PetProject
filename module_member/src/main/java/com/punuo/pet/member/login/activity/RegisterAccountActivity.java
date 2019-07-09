package com.punuo.pet.member.login.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.manager.ILoginCallBack;
import com.punuo.pet.member.login.manager.LoginManager;
import com.punuo.pet.router.CompatRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.view.CleanEditText;


/**
 * Created by han.chen.
 * Date on 2019-06-22.
 * 注册页面
 **/
@Route(path = MemberRouter.ROUTER_REGISTER_ACCOUNT_ACTIVITY)
public class RegisterAccountActivity extends BaseSwipeBackActivity {

    private TextView mTitle;
    private ImageView mBack;
    private TextView mStatusText;
    private CleanEditText mEditInput;
    private CleanEditText mEditPwd;
    private TextView mPasswordTip;
    private TextView mApply;
    @Autowired(name = "isRegister")
    boolean isRegister;
    @Autowired(name = "type")
    int type;
    @Autowired(name = "title")
    String title;

    private String stepOne = "<font color=\"#FF1A1A\">1.输入手机号</font> > 2.输入验证码 > 3.设置密码";
    private String stepTwo = "1.输入手机号 > <font color=\"#FF1A1A\">2.输入验证码</font> > 3.设置密码";
    private String stepThree = "1.输入手机号 > 2.输入验证码 > <font color=\"#FF1A1A\">3.设置密码</font>";
    public static final int TYPE_INPUT_PHONE = 1;
    public static final int TYPE_INPUT_CODE = 2;
    public static final int TYPE_SET_PWD = 3;
    private LoginManager mLoginManager;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        ARouter.getInstance().inject(this);
        mLoginManager = new LoginManager(this, mLoginCallBack);
        initView();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mBack = (ImageView) findViewById(R.id.back);
        mStatusText = (TextView) findViewById(R.id.status_text);
        mEditInput = (CleanEditText) findViewById(R.id.edit_input);
        mEditPwd = (CleanEditText) findViewById(R.id.edit_pwd);
        mPasswordTip = (TextView) findViewById(R.id.password_tip);
        mApply = (TextView) findViewById(R.id.apply);
        if (isRegister) {
            mTitle.setText(title);
        } else {
            mTitle.setText(title);
        }
        switchType(type);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
    }

    private void switchType(int type) {
        switch (type) {
            case TYPE_INPUT_PHONE:
                mEditPwd.setVisibility(View.GONE);
                mPasswordTip.setVisibility(View.GONE);
                mStatusText.setText(Html.fromHtml(stepOne));
                mEditInput.setText("");
                mEditInput.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                mEditInput.setHint("请输入您的手机号");
                mApply.setText("获取验证码");
                mApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPhone = mEditInput.getText().toString();
                        mLoginManager.sendAuthCode(mPhone);
                    }
                });
                break;
            case TYPE_INPUT_CODE:
                mEditPwd.setVisibility(View.GONE);
                mPasswordTip.setVisibility(View.GONE);
                mStatusText.setText(Html.fromHtml(stepTwo));
                mEditInput.setText("");
                mEditInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                mEditInput.setHint("请输入短信中的验证码");
                mApply.setText("提交验证码");
                mApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = mEditInput.getText().toString();
                        mLoginManager.loginWithPhone(mPhone, code);
                    }
                });
                break;
            case TYPE_SET_PWD:
                mEditPwd.setVisibility(View.VISIBLE);
                mPasswordTip.setVisibility(View.VISIBLE);
                mEditInput.setText("");
                mEditInput.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                mEditPwd.setText("");
                mEditPwd.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                mEditInput.setHint("输入密码");
                mEditPwd.setHint("确认密码");
                mStatusText.setText(Html.fromHtml(stepThree));
                mApply.setText("确认提交");
                mApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pwd = mEditInput.getText().toString();
                        String confirmPwd = mEditPwd.getText().toString();
                        mLoginManager.setPassword(pwd, confirmPwd);
                    }
                });
                break;
            default:
                break;
        }
    }

    private ILoginCallBack mLoginCallBack = new ILoginCallBack() {
        @Override
        public void loginSuccess() {
            switchType(TYPE_SET_PWD);
        }

        @Override
        public void loginError() {

        }

        @Override
        public void getAuthCodeSuccess() {
            switchType(TYPE_INPUT_CODE);
        }

        @Override
        public void getAuthCodeError() {

        }

        @Override
        public void setPasswordSuccess() {
            ARouter.getInstance().build(CompatRouter.ROUTER_HOME_ACTIVITY).navigation();
        }

        @Override
        public void logoutSuccess() {

        }
    };
}
