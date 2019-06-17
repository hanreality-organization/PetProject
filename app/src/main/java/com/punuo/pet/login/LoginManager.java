package com.punuo.pet.login;

import android.text.TextUtils;

import com.punuo.pet.account.AccountManager;
import com.punuo.pet.login.model.LoginResult;
import com.punuo.pet.login.request.AccountLoginRequest;
import com.punuo.pet.login.request.GetCodeRequest;
import com.punuo.pet.login.request.QuickLoginRequest;
import com.punuo.pet.model.BaseModel;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.RegexUtils;
import com.punuo.sys.sdk.util.ToastUtils;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class LoginManager {

    private BaseActivity mActivity;
    private ILoginCallBack mLoginCallBack;

    public LoginManager(BaseActivity activity, ILoginCallBack loginCallBack) {
        mActivity = activity;
        mLoginCallBack = loginCallBack;
    }

    private QuickLoginRequest mQuickLoginRequest;

    public void loginWithPhone(String phone, String code) {
        if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showToast("请输入合法的手机号码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast("请输入验证码");
            return;
        }
        if (mQuickLoginRequest != null && !mQuickLoginRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog("登陆中...");
        mQuickLoginRequest = new QuickLoginRequest();
        mQuickLoginRequest.addUrlParam("phone", phone);
        mQuickLoginRequest.addUrlParam("code", code);
        mQuickLoginRequest.setRequestListener(new RequestListener<LoginResult>() {
            @Override
            public void onComplete() {
                mActivity.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(LoginResult result) {
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }

                if (result.success) {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginSuccess();
                    }
                    AccountManager.setSession(result.session);
                } else {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginError();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                if (mLoginCallBack != null) {
                    mLoginCallBack.loginError();
                }
            }
        });
        HttpManager.addRequest(mQuickLoginRequest);
    }

    //获取验证码
    private GetCodeRequest mGetCodeRequest;

    public void sendAuthCode(String phone) {
        if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showToast("请输入合法的手机号码");
        }
        if (mGetCodeRequest != null && !mGetCodeRequest.isFinish()) {
            return;
        }
        //TODO 发送验证码
        mActivity.showLoadingDialog("验证码发送中...");
        mGetCodeRequest = new GetCodeRequest();
        mGetCodeRequest.addUrlParam("phone", phone);
        mGetCodeRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                mActivity.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetCodeRequest);
    }

    private AccountLoginRequest mAccountLoginRequest;

    public void loginWithAccount(String accountName, String pwd) {
        if (TextUtils.isEmpty(accountName)) {
            ToastUtils.showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast("请输入密码");
            return;
        }
        if (mAccountLoginRequest != null && !mAccountLoginRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog("登陆中...");
        mAccountLoginRequest = new AccountLoginRequest();
        mAccountLoginRequest.addUrlParam("userName", accountName);
        mAccountLoginRequest.addUrlParam("usePwd", pwd);
        mAccountLoginRequest.setRequestListener(new RequestListener<LoginResult>() {
            @Override
            public void onComplete() {
                mActivity.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(LoginResult result) {
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
                if (result.success) {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginSuccess();
                    }
                    AccountManager.setSession(result.session);
                } else {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginError();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                if (mLoginCallBack != null) {
                    mLoginCallBack.loginError();
                }
            }
        });
        HttpManager.addRequest(mAccountLoginRequest);
    }

    public void loginWithWeChat() {

    }

}
