package com.punuo.pet.feed.plan;

import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipRequestType;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

public class PlanToSipRequest extends BaseSipRequest {
    private String mPlanTime;
    private String mPlanName;
    private String mPlanCount;
    private String mUserName;


    public PlanToSipRequest(String planTime, String planName, String planCount, String userName) {
        setSipRequestType(SipRequestType.Notify);
        setTargetResponse("feed_plan_response");
        mPlanTime = planTime;
        mPlanName = planName;
        mPlanCount = planCount;
        mUserName = userName;
    }

    @Override
    public String getBody() {

        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();

        try {
            value.put("username", mUserName);
            value.put("time", mPlanTime);
            value.put("name", mPlanName);
            value.put("count", mPlanCount);
            body.put("feed_plan", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();

    }
}
