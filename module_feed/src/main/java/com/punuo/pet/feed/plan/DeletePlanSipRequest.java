package com.punuo.pet.feed.plan;

import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipRequestType;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

public class DeletePlanSipRequest extends BaseSipRequest {

    public DeletePlanSipRequest() {
        setSipRequestType(SipRequestType.Notify);
        setHasResponse(false);
    }

    @Override
    public String getBody() {

        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();

        try {
            body.put("to_get_feed_plan", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();

    }
}
