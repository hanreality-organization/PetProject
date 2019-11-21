package com.punuo.sip.service;

/**
 * Created by han.chen.
 * Date on 2019-08-20.
 **/
public class ServicePath {

    public static final String PATH_MEDIA = "/sip/media";
    public static final String PATH_NOTIFY = "/sip/notify";
    public static final String PATH_REGISTER = "/sip/negotiate_response";
    public static final String PATH_LOGIN = "/sip/login_response";
    public static final String PATH_DEV_NOTIFY = "/sip/dev_notify";

    public static final String PATH_ERROR = "/sip/error";
    public static final String PATH_START_VIDEO = "/sip/start_video";


    public static final String PATH_WEIGHT_RESPONSE = "/sip/weight";//喂食之后更新剩余粮食UI,手机端必须在线才能收到返回信息并成功更新

    public static final String  PATH_FEEDNOW_RESPONSE = "/sip/feed_now_response";

    public static final String PLAN_TOSIP = "/sip/feed_plan_response";

}
