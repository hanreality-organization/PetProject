package com.punuo.pet.router;

/**
 * Created by han.chen.
 * Date on 2019-06-26.
 **/
public class MemberRouter {
    private static final String PREFIX = "/member";
    private static final String PREFIX1 = "/activity";
    public static final String ROUTER_MEMBER_FRAGMENT = PREFIX + "/MemberFragment";
    public static final String ROUTER_LOGIN_ACTIVITY = PREFIX + "/LoginActivity";
    public static final String ROUTER_REGISTER_ACCOUNT_ACTIVITY = PREFIX + "/RegisterAccountActivity";
    public static final String ROUTER_FORGET_PASSWORD_ACTIVITY = PREFIX + "/ForgetPasswordActivity";
    public static final String ROUTER_BIND_PHONE_ACTIVITY = PREFIX + "/BindPhoneActivity";
    public static final String ROUTER_ADD_PET_ACTIVITY = PREFIX + "/AddPetActivity";

    //账号管理部分
    public static final String ROUTER_ACCOUNT_MANAGEMENT_ACTIVITY = PREFIX1 + "/AccountManagementActivity";
    public static final String ROUTER_SET_NICKNAME_ACTIVITY = PREFIX1 + "/SetNicknameActivity";
    public static final String ROUTER_CHANGE_PASSWORD_ACTIVITY = PREFIX1 +"/ChangePasswordActivity";

    //系统部分
    public static final String ROUTER_SYSTEM_NEWS_ACTIVITY = PREFIX1+"/SystemNewsActivity";

    //关于我们
    public static final String ROUTER_ABOUT_ACTIVITY = PREFIX1+"/AboutACTIVITY";

    //客服
    public static final String ROUTER_CUETOMERSERVICE = PREFIX1+"/CustomerServiceActivity";

    //修改个人信息
    public static final String ROUTER_EDITUSERINFO_ACTIVITY = PREFIX1+"/EditUserInfoActivity";

    public static final String ROUTER_TEST_ACTIVITY = PREFIX1+"/TestActivity";
}
