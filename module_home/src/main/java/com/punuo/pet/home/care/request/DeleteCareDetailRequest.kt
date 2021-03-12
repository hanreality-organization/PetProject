package com.punuo.pet.home.care.request

import com.punuo.pet.home.care.model.CareDetailModel
import com.punuo.sys.sdk.httplib.BaseRequest
import com.punuo.sys.sdk.model.BaseModel

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
class DeleteCareDetailRequest : BaseRequest<BaseModel>() {
    init {
        setRequestPath("/dailycare/delThemeNew")
        setRequestType(RequestType.GET)
    }
}