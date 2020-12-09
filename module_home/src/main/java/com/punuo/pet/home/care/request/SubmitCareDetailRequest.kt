package com.punuo.pet.home.care.request

import com.punuo.sys.sdk.httplib.BaseRequest
import com.punuo.sys.sdk.model.BaseModel

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
class SubmitCareDetailRequest : BaseRequest<BaseModel>() {
    init {
        setRequestPath("/dailycare/addThemeNew")
        setRequestType(RequestType.POST)
    }
}