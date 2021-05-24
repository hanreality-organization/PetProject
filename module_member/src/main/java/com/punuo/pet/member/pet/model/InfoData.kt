package com.punuo.pet.member.pet.model

import com.punuo.pet.member.R
import com.punuo.sys.sdk.PnApplication
import com.punuo.sys.sdk.model.LanguageModel

/**
 * Created by han.chen.
 * Date on 2020/10/28.
 **/
object InfoData {
    private var petType: ArrayList<String>? = null
    fun getPetTypeList(): MutableList<String> {
        if (petType == null) {
            petType = ArrayList()
            petType?.add(PnApplication.getInstance().getString(R.string.string_british_shorthair))
            petType?.add(PnApplication.getInstance().getString(R.string.string_usa_shorthair))
            petType?.add(PnApplication.getInstance().getString(R.string.string_exotic_shorthair))
            petType?.add(PnApplication.getInstance().getString(R.string.string_other))
        }
        return petType ?: ArrayList()
    }

    private var petSex: ArrayList<String>? = null
    fun getPetSexList(): MutableList<String> {
        if (petSex == null) {
            petSex = ArrayList()
            petSex?.add(PnApplication.getInstance().getString(R.string.string_pet_male))
            petSex?.add(PnApplication.getInstance().getString(R.string.string_pet_female))
        }
        return petSex ?: ArrayList()
    }

    private var userSex: ArrayList<String>? = null
    fun getUserSexList(): MutableList<String> {
        if (userSex == null) {
            userSex = ArrayList()
            userSex?.add(PnApplication.getInstance().getString(R.string.string_male))
            userSex?.add(PnApplication.getInstance().getString(R.string.string_female))
        }
        return userSex ?: ArrayList()
    }

    private var languageList :ArrayList<LanguageModel>? = null
    fun getLanguageList():MutableList<LanguageModel> {
        if (languageList == null) {
            languageList = ArrayList()
            languageList?.add(LanguageModel(PnApplication.getInstance().getString(R.string.string_language_zh), "zh","CN"))
            languageList?.add(LanguageModel(PnApplication.getInstance().getString(R.string.string_language_en), "en",""))
        }
        return languageList ?: ArrayList()
    }

}