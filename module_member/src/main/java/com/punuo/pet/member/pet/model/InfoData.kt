package com.punuo.pet.member.pet.model

/**
 * Created by han.chen.
 * Date on 2020/10/28.
 **/
object InfoData {
    private var petType: ArrayList<String>? = null
    fun getPetTypeList(): MutableList<String> {
        if (petType == null) {
            petType = ArrayList()
            petType?.add("英国短毛猫")
            petType?.add("美国短毛猫")
            petType?.add("异国短毛猫")
            petType?.add("其他")
        }
        return petType ?: ArrayList()
    }

    private var petSex: ArrayList<String>? = null
    fun getPetSexList(): MutableList<String> {
        if (petSex == null) {
            petSex = ArrayList()
            petSex?.add("公")
            petSex?.add("母")
        }
        return petSex ?: ArrayList()
    }

    private var userSex: ArrayList<String>? = null
    fun getUserSexList(): MutableList<String> {
        if (userSex == null) {
            userSex = ArrayList()
            userSex?.add("男")
            userSex?.add("女")
        }
        return userSex ?: ArrayList()
    }


}