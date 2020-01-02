package com.punuo.pet.feed.plan;

import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.request.SipRequestType;
import com.punuo.sys.sdk.account.AccountManager;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;

public class DeletePlanSipRequest extends BaseSipRequest {

    private String name;
    public DeletePlanSipRequest(String name){
        setSipRequestType(SipRequestType.Notify);
        setTargetResponse("");
        this.name =name;
    }
    @Override
    public String getBody() {

        JSONObject body = new JSONObject();
        JSONObject value = new JSONObject();

        try {
            value.put("userName", AccountManager.getUserName());
            value.put("name",name);
            body.put("delete_plan",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonToXml jsonToXml = new JsonToXml.Builder(body).build();
        return jsonToXml.toFormattedString();

    }
}
