package com.punuo.pet.home.device.request

import com.punuo.sys.sdk.httplib.BaseRequest
import com.punuo.sys.sdk.model.BaseModel


/**
 * Created by han.chen.
 * Date on 2020/10/10.
 * 提交绑定审核
 * devid username
 **/
class AddBindingCheckRequest :BaseRequest<BaseModel>() {
    init {
        setRequestType(RequestType.GET)
        setRequestPath("/groups/addBindingCheck")
    }
}