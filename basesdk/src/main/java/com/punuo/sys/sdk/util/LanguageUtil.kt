package com.punuo.sys.sdk.util

import android.content.Context
import android.os.Build
import android.os.LocaleList
import com.google.gson.reflect.TypeToken
import com.punuo.sys.sdk.httplib.JsonUtil
import com.punuo.sys.sdk.model.LanguageModel
import java.util.*

/**
 * Created by han.chen.
 * Date on 2021/5/24.
 **/
object LanguageUtil {
    private const val SP_LANGUAGE = "sp_language"
    fun changeLanguage(context: Context, languageModel:LanguageModel?) {
        if (languageModel == null) {
            //跟随系统语言
            MMKVUtil.setString(SP_LANGUAGE, null)
        } else {
            val local = Locale(languageModel.language, languageModel.country)
            setAppLanguage(context, local)
            MMKVUtil.setString(SP_LANGUAGE, JsonUtil.toJson(languageModel))
        }
    }

    fun setAppLanguage(context: Context, local:Locale) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(local)
            configuration.locales = LocaleList(local)
            context.createConfigurationContext(configuration)
            resources.updateConfiguration(configuration,metrics)
        } else {
            configuration.setLocale(local)
            resources.updateConfiguration(configuration,metrics)
        }
    }

    fun attachBaseContext(context: Context) : Context {
        val languageModel = getCurrentLanguageModel()
        languageModel?.let {
            val locale = Locale(it.language, it.country)
            setAppLanguage(context, locale)
        }
        setConfiguration(context)
        return context
    }

    fun getCurrentLanguageModel(): LanguageModel? {
        return try {
            JsonUtil.fromJson<LanguageModel>(
                MMKVUtil.getString(SP_LANGUAGE), object : TypeToken<LanguageModel>() {}.type
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getSystemLanguageName() :String {
        return Locale.getDefault().displayLanguage
    }

    fun getCurrentLanguage() :String {
        val language = getCurrentLanguageModel()?.language
        return language?: Locale.getDefault().language
    }

    fun setConfiguration(context: Context?) {
        context?.applicationContext?.let {
            val configuration = it.resources.configuration
            configuration.setLocale(getSysPreferredLocale())
            val resources = it.resources
            val dm = resources.displayMetrics
            resources.updateConfiguration(configuration, dm)
        }
    }

    private fun getSysPreferredLocale() :Locale {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Locale.getDefault()
        } else {
            LocaleList.getDefault()[0]
        }
    }
}