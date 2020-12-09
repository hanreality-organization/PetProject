package com.punuo.pet.home.care.request

import com.punuo.pet.home.care.model.CareDetailModel
import com.punuo.sys.sdk.httplib.BaseRequest

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
class GetCareDetailRequest : BaseRequest<CareDetailModel>() {
    init {
        setRequestPath("/dailycare/getThemeNew")
        setRequestType(RequestType.GET)
    }
}