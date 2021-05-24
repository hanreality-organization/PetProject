package com.punuo.sys.sdk.model

import com.contrarywind.interfaces.IPickerViewData

/**
 * Created by han.chen.
 * Date on 2021/5/24.
 **/
data class LanguageModel(var languageName:String, var language:String, var country:String) : IPickerViewData {
    override fun getPickerViewText(): String {
        return languageName
    }

}
