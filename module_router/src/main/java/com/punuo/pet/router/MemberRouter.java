package com.punuo.pet.router;

/**
 * Created by han.chen.
 * Date on 2019-06-26.
 **/
public class MemberRouter {
    private static final String PREFIX = "/member";
    public static final String ROUTER_MEMBER_FRAGMENT = PREFIX + "/";
    public static final String ROUTER_LOGIN_ACTIVITY = PREFIX + "/LoginActivity";
    public static final String ROUTER_REGISTER_ACCOUNT_ACTIVITY = PREFIX + "/RegisterAccountActivity";
    public static final String ROUTER_FORGET_PASSWORD_ACTIVITY = PREFIX + "/ForgetPasswordActivity";
    public static final String ROUTER_BIND_PHONE_ACTIVITY = PREFIX + "/BindPhoneActivity";
    public static final String ROUTER_ADD_PET_ACTIVITY = PREFIX + "/AddPetActivity";
    public static final String ROUTER_CREATE_PET_ACTIVITY = PREFIX + "/CreatePetActivity";

    //账号管理部分
    public static final String ROUTER_ACCOUNT_MANAGEMENT_ACTIVITY = PREFIX + "/AccountManagementActivity";
    public static final String ROUTER_SET_NICKNAME_ACTIVITY = PREFIX + "/SetNicknameActivity";
    public static final String ROUTER_CHANGE_PASSWORD_ACTIVITY = PREFIX + "/ChangePasswordActivity";

    //系统部分
    public static final String ROUTER_SYSTEM_NEWS_ACTIVITY = PREFIX + "/SystemNewsActivity";

    //关于我们
    public static final String ROUTER_ABOUT_ACTIVITY = PREFIX + "/AboutACTIVITY";

    //客服
    public static final String ROUTER_CUETOMERSERVICE = PREFIX + "/CustomerServiceActivity";

    //修改个人信息
    public static final String ROUTER_EDITUSERINFO_ACTIVITY = PREFIX + "/EditUserInfoActivity";
}
