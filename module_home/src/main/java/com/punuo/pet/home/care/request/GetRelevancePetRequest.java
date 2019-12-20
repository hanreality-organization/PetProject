package com.punuo.pet.home.care.request;

import com.punuo.pet.home.care.model.PetData;
import com.punuo.pet.home.care.model.PetNameListModel;
import com.punuo.sys.sdk.httplib.BaseRequest;

public class GetRelevancePetRequest extends BaseRequest<PetNameListModel> {

    public GetRelevancePetRequest(){
        setRequestPath("/");
        setRequestType(RequestType.GET);
    }
}
