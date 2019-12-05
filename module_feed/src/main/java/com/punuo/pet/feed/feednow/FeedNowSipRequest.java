package com.punuo.pet.feed.feednow;

import com.alibaba.fastjson.JSON;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipRequestType;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;


public class FeedNowSipRequest extends BaseSipRequest {

    private String mUserName;
    private String mFeedCount;//喂食的份数


    public FeedNowSipRequest(String username,String feedcount){
        setSipRequestType(SipRequestType.Notify);
        setTargetResponse("feed_now_response");
        mUserName = username;
        mFeedCount = feedcount;
    }

    @Override
    public String getBody() {

        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();

        try {
            value.put("from",mUserName);
            value.put("feed_count",mFeedCount);
            body.put("feed_now",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();

    }
}
