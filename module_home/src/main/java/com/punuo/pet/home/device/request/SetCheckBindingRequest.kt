package com.punuo.pet.home.device.request

import com.punuo.pet.home.device.model.SelfHost
import com.punuo.sys.sdk.httplib.BaseRequest
import com.punuo.sys.sdk.model.BaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/22.
 **/
class SetCheckBindingRequest: BaseRequest<BaseModel>() {

    init {
        setRequestType(RequestType.GET)
        setRequestPath("groups/CheckBinding")
    }
}