package com.punuo.pet.home.pet.request

import com.punuo.sys.sdk.httplib.BaseRequest
import com.punuo.sys.sdk.model.BaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/23.
 **/
class DeletePetRequest :BaseRequest<BaseModel>() {

    init {
        setRequestType(RequestType.GET)
        setRequestPath("/pets/delPet")
    }
}