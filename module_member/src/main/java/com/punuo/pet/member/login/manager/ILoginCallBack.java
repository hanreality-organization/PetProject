package com.punuo.pet.member.login.manager;

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
