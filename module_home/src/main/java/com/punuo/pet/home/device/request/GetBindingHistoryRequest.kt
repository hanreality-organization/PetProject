package com.punuo.pet.home.device.request

import com.punuo.pet.home.device.model.BindHistoryModel
import com.punuo.sys.sdk.httplib.BaseRequest

/**
 * Created by han.chen.
 * Date on 2020/10/25.
 **/
class GetBindingHistoryRequest : BaseRequest<BindHistoryModel>() {
    init {
        setRequestPath("/groups/getBindingHistory")
        setRequestType(RequestType.GET)
    }
}