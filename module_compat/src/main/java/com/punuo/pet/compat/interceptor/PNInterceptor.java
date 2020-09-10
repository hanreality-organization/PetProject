package com.punuo.pet.compat.interceptor;

import com.punuo.sys.sdk.util.DeviceHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by han.chen.
 * Date on 2020/9/10.
 **/
public class PNInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originReq = chain.request();
        final Request.Builder builder = originReq.newBuilder();

        builder.addHeader("APP_VERSION_NAME", DeviceHelper.getVersionName());
        builder.addHeader("APP_VERSION_CODE", String.valueOf(DeviceHelper.getVersionCode()));
        final Request newRequest = builder.build();
        Response response = null;
        try {
            response = chain.proceed(newRequest);
        } catch (Throwable e) {
            throw e;
        }
        return response;
    }
}
