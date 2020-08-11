package com.punuo.pet.feed.get_real_weight;

import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipRequestType;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

public class UpdateWeightSipRequest extends BaseSipRequest {
    private String mUsername;

    public UpdateWeightSipRequest(String username){
        setSipRequestType(SipRequestType.Notify);
        setTargetResponse("update_weight_response");
        mUsername = username;
    }

    @Override
    public String getBody() {
        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();

        try {
            value.put("username",mUsername);
            body.put("update_weight",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();
    }
}
