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
                mStatusText.setText(Html.fromHtml(getString(R.string.string_one_step)));
                mEditInput.setText("");
                mEditInput.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                mEditInput.setHint(getString(R.string.string_edit_phone_number_hint));
                mApply.setText(getString(R.string.string_get_code));
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
                mStatusText.setText(Html.fromHtml(getString(R.string.string_two_step)));
                mEditInput.setText("");
                mEditInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                mEditInput.setHint(getString(R.string.string_input_code));
                mApply.setText(R.string.string_submit_code);
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
                mEditInput.setHint(getString(R.string.string_password_input));
                mEditPwd.setHint(R.string.string_confirm_password);
                mStatusText.setText(Html.fromHtml(getString(R.string.string_three_step)));
                mApply.setText(R.string.string_confirm_submit);
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
