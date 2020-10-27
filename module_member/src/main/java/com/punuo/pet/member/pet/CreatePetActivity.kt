package com.punuo.pet.member.pet

import android.os.Bundle
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.punuo.pet.member.R
import com.punuo.pet.model.PetData
import com.punuo.pet.router.MemberRouter
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity

/**
 * Created by han.chen.
 * Date on 2020/10/27.
 **/
@Route(path = MemberRouter.ROUTER_CREATE_PET_ACTIVITY)
class CreatePetActivity : BaseSwipeBackActivity() {

    @JvmField
    @Autowired(name = "petData")
    var petData: PetData? = null
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet)
        ARouter.getInstance().inject(this)
        initView()
    }

    private fun initView() {
        titleText = findViewById(R.id.title) as TextView
        titleText.text = if (petData == null) "添加宠物" else "编辑宠物"
    }
}