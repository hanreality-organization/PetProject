package com.punuo.sip.service;

import android.content.Context;

import com.google.gson.JsonElement;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipResponseRequest;
import com.punuo.sys.sdk.httplib.JsonUtil;

import org.zoolu.sip.message.Message;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by han.chen.
 * Date on 2019-08-21.
 * 已经统一做了response
 **/
public abstract class NormalRequestService<T> implements SipRequestService {
    protected final String TAG = "NormalRequestService";
    protected SipResponseRequest mSipResponseRequest;

    @Override
    public void handleRequest(Message msg, JsonElement jsonElement) {
        try {
            T result = jsonParse(jsonElement, jsonElement.getAsJsonObject().toString());
            onSuccess(msg, result);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    public void init(Context context) {
        mSipResponseRequest = new SipResponseRequest();
    }

    protected abstract String getBody();

    protected abstract void onSuccess(Message msg, T result);

    protected abstract void onError(Exception e);

    protected void onResponse(Message msg) {
        mSipResponseRequest.setResponse(msg, 200, "OK");
        mSipResponseRequest.setBody(getBody());
        SipUserManager.getInstance().addRequest(mSipResponseRequest);
    }

    protected T jsonParse(JsonElement result, String response) {
        if (result == null) {
            return jsonParse(response);
        }
        Type type = getType();
        if (type == String.class) {
            return (T) response;
        }
        return JsonUtil.fromJsonNoCatch(result, getType());
    }

    protected T jsonParse(String result) {
        Type type = getType();
        if (type == String.class) {
            return (T) result;
        }
        return JsonUtil.fromJson(result, getType());
    }

    public Type getType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }
}
