package com.punuo.pet.router;

/**
 * Created by han.chen.
 * Date on 2019-06-26.
 **/
public class HomeRouter {
    private static final String PREFIX = "/home";
    public static final String ROUTER_HOME_FRAGMENT = PREFIX + "/HomeFragment";
    public static final String ROUTER_CARE_ACTIVITY = PREFIX + "/CareActivity";
    public static final String ROUTER_CHART_ACTIVITY = PREFIX + "/ChartActivity";
    public static final String ROUTER_BIND_DEVICE_ACTIVITY = PREFIX + "/BindDeviceActivity";
    public static final String ROUTER_DEVICE_MANAGER_ACTIVITY = PREFIX + "/DeviceManagerActivity";
    public static final String ROUTER_SELECT_DEVICE_ACTIVITY = PREFIX + "/SelectDeviceActivity";

    //日常护理子项activity
    public static final String ROUTER_CARE_BATH_ACTIVITY = PREFIX + "/BathActivity";
    public static final String ROUTER_CARE_CHECKUP_ACTIVITY = PREFIX + "/CheckupActivity";
    public static final String ROUTER_CARE_BUY_FOOD_ACTIVITY = PREFIX + "/BuyFoodActivity";
    public static final String ROUTER_CARE_IN_VIVO_ACTIVITY = PREFIX + "/DewormingVivoActivity";
    public static final String ROUTER_CARE_IN_VITRO_ACTIVITY = PREFIX + "/DewormingVitroActivity";
    public static final String ROUTER_CARE_VACCINE_ACTIVITY = PREFIX + "/VaccineActivity";
    public static final String ROUTER_CARE_BEAUTY_ACTIVITY = PREFIX + "/BeautyActivity";
    public static final String ROUTER_CARE_WALK_ACTIVITY = PREFIX + "/WalkActivity";

    public static final String ROUTER_HOTSPOT_CONNECT_WIFI = PREFIX + "/HotSpotConnectWifi";
    public static final String ROUTER_WIFI_CHOOSE_ACTIVITY = PREFIX + "/WifiChooseActivity";
}
