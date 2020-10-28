package com.punuo.pet.member.pet.request;

import com.punuo.sys.sdk.httplib.BaseRequest;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2019-07-10.
 **/
public class UpdatePetRequest extends BaseRequest<BaseModel> {

    public UpdatePetRequest() {
        setRequestPath("/pets/updatePetInfo");
        setRequestType(RequestType.POST);
    }
}
