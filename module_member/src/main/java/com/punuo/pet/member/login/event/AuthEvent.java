package com.punuo.pet.member.login.event;

import android.os.Bundle;

/**
 * Created by han.chen.
 * Date on 2019-06-22.
 **/
public class AuthEvent {
    public static final int TYPE_SINA = 0;
    public static final int TYPE_QQ = 1;
    public static final int TYPE_WEIXIN = 2;
    public String id;
    public String token;
    public int type;
    public Bundle bundle;

    public AuthEvent(int type, String id, String token) {
        this.type = type;
        this.id = id;
        this.token = token;
    }

    public AuthEvent(int type, Bundle bundle) {
        this.type = type;
        this.bundle = bundle;
    }
}
