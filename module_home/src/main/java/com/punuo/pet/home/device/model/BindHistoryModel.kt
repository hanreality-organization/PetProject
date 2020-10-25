package com.punuo.pet.home.device.model

import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
data class BindHistoryModel(var success: Boolean,
                            var message: String?,
                            var data: Data?) : PNBaseModel() {
    data class Data(var items: MutableList<BindHistoryData>?) : PNBaseModel()
}