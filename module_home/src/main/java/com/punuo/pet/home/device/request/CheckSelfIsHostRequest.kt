package com.punuo.pet.home.device.request

import com.punuo.pet.home.device.model.SelfHost
import com.punuo.sys.sdk.httplib.BaseRequest

/**
 * Created by han.chen.
 * Date on 2020/10/22.
 **/
class CheckSelfIsHostRequest: BaseRequest<SelfHost>() {

    init {
        setRequestType(RequestType.GET)
        setRequestPath("/users/amIHost")
    }
}