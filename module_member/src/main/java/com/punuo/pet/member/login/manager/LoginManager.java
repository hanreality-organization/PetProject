package com.punuo.pet.member.login.manager;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
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
            ToastUtils.showToast("请输入合法的手机号码");
            return;
        }
        if (mGetCodeRequest != null && !mGetCodeRequest.isFinish()) {
            return;
        }
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
                } else {
                    ToastUtils.showToast("验证短信已经发送到" + RegexUtils.hidePhone(phone));
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
            ToastUtils.showToast("您还没有安装微信");
            mActivity.dismissLoadingDialog();
        }
    }

    private SetPasswordRequest mSetPasswordRequest;

    public void setPassword(String password, String confirmPwd) {
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtils.showToast("请输入确认密码");
            return;
        }
        if (mSetPasswordRequest != null && !mSetPasswordRequest.isFinish()) {
            return;
        }
        mActivity.showLoadingDialog("提交中...");
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
