package com.punuo.sys.sdk.activity

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.punuo.pet.router.CompatRouter
import com.punuo.pet.router.SDKRouter
import com.punuo.sys.sdk.R

/**
 * Created by han.chen.
 * Date on 2021/5/24.
 **/
@Route(path = SDKRouter.ROUTER_LANGUAGE_ENTRY_ACTIVITY)
class LanguageEntryActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_entry)
        mBaseHandler.postDelayed({
            ARouter.getInstance().build(CompatRouter.ROUTER_HOME_ACTIVITY)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation()
            finish()
        }, 1000)
    }
}