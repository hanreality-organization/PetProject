package com.punuo.pet.home.device.model

import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/10.
 **/
data class SelfHost(var data: Data) : BaseModel() {
    data class Data(var host: Boolean) : PNBaseModel()
}