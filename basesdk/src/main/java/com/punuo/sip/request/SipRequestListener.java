package com.punuo.sip.request;

import org.zoolu.sip.message.Message;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 **/
public interface SipRequestListener<T> {
    void onComplete();
    void onSuccess(T result, Message message);
    void onError(Exception e);
}
