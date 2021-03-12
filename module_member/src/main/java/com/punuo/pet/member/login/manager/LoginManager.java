package com.punuo.pet.member.login.manager;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.login.model.LoginResult;
import com.punuo.pet.member.login.request.AccountLoginRequest;
import com.punuo.pet.member.login.request.GetCodeRequest;
import com.punuo.pet.member.login.request.QuickLoginRequest;
import com.punuo.pet.member.login.request.SetPasswordRequest;
import com.punuo.pet.member.login.request.WeChatLoginRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.DeviceHelper;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.RegexUtils;
import com.punuo.sys.sdk.util.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


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

    public void loginWithPhone(final String phone, String code) {
        if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_legal_phone_number));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_input_code));
            return;
        }
        if (mQuickLoginRequest != null && !mQuickLoginRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog(mActivity.getString(R.string.string_login_ing));
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
                Log.i("loginsuccess", "onSuccess: "+result);
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }

                if (result.success) {
                    AccountManager.setSession(result.session);
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginSuccess();
                    }
                } else {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginError();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
                if (mLoginCallBack != null) {
                    mLoginCallBack.loginError();
                }
            }
        });
        HttpManager.addRequest(mQuickLoginRequest);
    }

    //获取验证码
    private GetCodeRequest mGetCodeRequest;

    public void sendAuthCode(final String phone) {
        if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_legal_phone_number));
            return;
        }
        if (mGetCodeRequest != null && !mGetCodeRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog(mActivity.getString(R.string.string_code_sending));
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
                } else {
                    ToastUtils.showToast(mActivity.getString(R.string.string_code_have_send, RegexUtils.hidePhone(phone)));
                }
                if (result.success) {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.getAuthCodeSuccess();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
                if (mLoginCallBack != null) {
                    mLoginCallBack.getAuthCodeError();
                }
            }
        });
        HttpManager.addRequest(mGetCodeRequest);
    }

    private AccountLoginRequest mAccountLoginRequest;

    public void loginWithAccount(final String phone, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_account_input));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_password_input));
            return;
        }
        if (mAccountLoginRequest != null && !mAccountLoginRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog(mActivity.getString(R.string.string_login_ing));
        mAccountLoginRequest = new AccountLoginRequest();
        mAccountLoginRequest.addUrlParam("userName", phone);
        mAccountLoginRequest.addUrlParam("userPwd", pwd);
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
                    AccountManager.setSession(result.session);
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginSuccess();
                    }
                } else {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.loginError();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
                if (mLoginCallBack != null) {
                    mLoginCallBack.loginError();
                }
            }
        });
        HttpManager.addRequest(mAccountLoginRequest);
    }

    private WeChatLoginRequest mWeChatLoginRequest;

    public void loginWithWeChat(String code) {
        if (mWeChatLoginRequest != null && !mWeChatLoginRequest.isFinish()) {
            return;
        }
        mWeChatLoginRequest = new WeChatLoginRequest();
        mWeChatLoginRequest.addUrlParam("code", code);
        mWeChatLoginRequest.setRequestListener(new RequestListener<LoginResult>() {
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
                    AccountManager.setSession(result.session);
                    if (!result.hasBindPhone) {
                        //绑定手机
                        Bundle params = new Bundle();
                        params.putString("openId", result.session);
                        ARouter.getInstance().build(MemberRouter.ROUTER_BIND_PHONE_ACTIVITY)
                                .with(params)
                                .navigation();
                    }
                    mLoginCallBack.loginSuccess();
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
                if (mLoginCallBack != null) {
                    mLoginCallBack.loginError();
                }
            }
        });
        HttpManager.addRequest(mWeChatLoginRequest);
    }

    private IWXAPI mIWXAPI;

    private void createWxApi(Context context) {
        if (mIWXAPI == null) {
            mIWXAPI = WXAPIFactory.createWXAPI(context.getApplicationContext(),
                    Constant.WX_APP_ID, false);
            mIWXAPI.registerApp(Constant.WX_APP_ID);
        }

    }

    public void authLogin(Context context) {
        createWxApi(context);
        if (DeviceHelper.isWechatInstalled(context, Constant.WX_APP_ID)) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = String.valueOf(System.currentTimeMillis());
            mIWXAPI.sendReq(req);
        } else {
            ToastUtils.showToast(context.getString(R.string.string_wx_not_install));
            mActivity.dismissLoadingDialog();
        }
    }

    private SetPasswordRequest mSetPasswordRequest;

    public void setPassword(String password, String confirmPwd) {
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_login_ing));
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtils.showToast(mActivity.getString(R.string.string_check_password));
            return;
        }
        if (mSetPasswordRequest != null && !mSetPasswordRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog(mActivity.getString(R.string.string_commit_ing));
        mSetPasswordRequest = new SetPasswordRequest();
        mSetPasswordRequest.addUrlParam("password", password);
        mSetPasswordRequest.addUrlParam("confirm_pwd", confirmPwd);
        mSetPasswordRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                mActivity.dismissLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    if (mLoginCallBack != null) {
                        mLoginCallBack.setPasswordSuccess();
                    }
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(mSetPasswordRequest);
    }
}
