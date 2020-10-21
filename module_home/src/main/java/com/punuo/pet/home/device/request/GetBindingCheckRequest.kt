package com.punuo.pet.home.device.request

import com.punuo.pet.home.device.model.DeviceBindModel
import com.punuo.sys.sdk.httplib.BaseRequest

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 * devid username
 * 获取申请列表
 **/
class GetBindingCheckRequest : BaseRequest<DeviceBindModel>() {

    init {
        setRequestType(RequestType.GET)
        setRequestPath("/groups/getBindingCheck")
    }
}