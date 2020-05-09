package com.punuo.pet.router;

/**
 * Created by han.chen.
 * Date on 2019-06-26.
 **/
public class HomeRouter {
    private static final String PREFIX = "/home";
    public static final String ROUTER_HOME_FRAGMENT = PREFIX + "/HomeFragment";
    public static final String ROUTER_CONNECT_DEVICE_ACTIVITY = PREFIX + "/ConnectDeviceActivity";
    public static final String ROUTER_CONNECT_RESULT_ACTIVITY = PREFIX + "/ConnectResultActivity";
    public static final String ROUTER_CARE_ACTIVITY = PREFIX + "/CareActivity";
    public static final String ROUTER_BLUETOOTH_ACTIVITY = PREFIX + "/BlueToothActivity";
    public static final String ROUTER_WIFI_CONNECT_ACTIVITY = PREFIX + "/WifiConnectActivity";
    public static final String ROUTER_BIND_DEVICE_ACTIVITY = PREFIX + "/BindDeviceActivity";
    public static final String ROUTER_SELECT_DEVICE_ACTIVITY = PREFIX + "/SelectDeviceActivity";

    //日常护理子项activity
    public static final String ROUTER_CARE_BATH_ACTIVITY = PREFIX+"/BathActivity";
    public static final String ROUTER_CARE_CHECKUP_ACTIVITY = PREFIX+"/CheckupActivity";
    public static final String ROUTER_CARE_BUY_FOOD_ACTIVITY = PREFIX+"/BuyFoodActivity";
    public static final String ROUTER_CARE_IN_VIVO_ACTIVITY = PREFIX+"/DewormingVivoActivity";
    public static final String ROUTER_CARE_IN_VITRO_ACTIVITY = PREFIX+"/DewormingVitroActivity";
    public static final String ROUTER_CARE_VACCINE_ACTIVITY = PREFIX+"/VaccineActivity";
    public static final String ROUTER_CARE_BEAUTY_ACTIVITY = PREFIX+"/BeautyActivity";
    public static final String ROUTER_CARE_WALK_ACTIVITY = PREFIX+"/WalkActivity";

    public static final String ROUTER_HOTSPOT_CONNECT_WIFI=PREFIX+"/HotSpotConnectWifi";
}
