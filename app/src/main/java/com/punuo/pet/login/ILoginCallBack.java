package com.punuo.pet.login;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public interface ILoginCallBack {
    void loginSuccess();
    void loginError();
    void getAuthCodeSuccess();
    void getAuthCodeError();
    void setPasswordSuccess();
    void logoutSuccess();
}
