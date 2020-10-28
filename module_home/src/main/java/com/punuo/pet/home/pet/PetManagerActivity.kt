package com.punuo.pet.home.pet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.punuo.pet.PetManager
import com.punuo.pet.event.UpdatePetEvent
import com.punuo.pet.home.R
import com.punuo.pet.home.pet.adapter.PetManagerAdapter
import com.punuo.pet.model.PetModel
import com.punuo.pet.router.HomeRouter
import com.punuo.pet.router.MemberRouter
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by han.chen.
 * Date on 2020/10/23.
 **/
@Route(path = HomeRouter.ROUTER_PET_MANAGER_ACTIVITY)
class PetManagerActivity : BaseSwipeBackActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var backImage: View
    private lateinit var titleText: TextView
    private lateinit var subTitle: TextView

    private var adapter: PetManagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_manager_activity)
        EventBus.getDefault().register(this)
        initView()
    }

    private fun initView() {
        recycler = findViewById(R.id.recycler) as RecyclerView
        emptyText = findViewById(R.id.empty_text) as TextView
        backImage = findViewById(R.id.back)
        titleText = findViewById(R.id.title) as TextView
        subTitle = findViewById(R.id.sub_title) as TextView
        titleText.text = "宠物管理"
        subTitle.text ="添加"
        subTitle.visibility = View.VISIBLE
        backImage.setOnClickListener { onBackPressed() }
        subTitle.setOnClickListener {
            ARouter.getInstance().build(MemberRouter.ROUTER_PET_INFO_ACTIVITY)
                    .withBoolean("canEdit", true)
                    .navigation()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = PetManagerAdapter(this, ArrayList())
        recycler.adapter = adapter
        refresh()
    }

    private fun refresh() {
        PetManager.getPetInfo(false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(model: PetModel) {
        if (model.mPets == null || model.mPets.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            adapter?.clear()
            return
        }
        emptyText.visibility = View.GONE
        adapter?.clear()
        adapter?.addAll(model.mPets)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}