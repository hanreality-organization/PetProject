package com.punuo.pet.home.care

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView
import com.punuo.pet.home.R
import com.punuo.pet.home.care.adapter.CareDetailAdapter
import com.punuo.pet.home.care.event.AddCareDetailEvent
import com.punuo.pet.home.care.model.CareDetailModel
import com.punuo.pet.home.care.request.DeleteCareDetailRequest
import com.punuo.pet.home.care.request.GetCareDetailRequest
import com.punuo.pet.model.PetData
import com.punuo.pet.router.HomeRouter
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.util.HandlerExceptionUtils
import com.punuo.sys.sdk.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
@Route(path = HomeRouter.ROUTER_CARE_DETAIL_ACTIVITY)
class CareDetailActivity : BaseSwipeBackActivity() {

    @JvmField
    @Autowired(name = "title")
    var title: String? = null

    @JvmField
    @Autowired(name = "theme")
    var theme: String? = null

    @JvmField
    @Autowired(name = "petData")
    var petData: PetData? = null

    private lateinit var titleView: TextView
    private lateinit var backView: View
    private lateinit var emptyText: TextView
    private lateinit var pullToRefreshRecyclerView: PullToRefreshRecyclerView
    private lateinit var recycler: RecyclerView
    private lateinit var addButton: View

    private lateinit var adapter: CareDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.care_detail_activity)
        ARouter.getInstance().inject(this)
        initView()
        getData()
        EventBus.getDefault().register(this)
    }

    fun initView() {
        titleView = findViewById(R.id.title) as TextView
        titleView.text = title ?: "日常护理"

        backView = findViewById(R.id.back)
        backView.setOnClickListener {
            scrollToFinishActivity()
        }
        emptyText = findViewById(R.id.empty_text) as TextView
        addButton = findViewById(R.id.add_button)
        pullToRefreshRecyclerView = findViewById(R.id.recycler) as PullToRefreshRecyclerView
        recycler = pullToRefreshRecyclerView.refreshableView
        recycler.layoutManager = LinearLayoutManager(this)
        pullToRefreshRecyclerView.setOnRefreshListener {
            getData()
        }
        adapter = CareDetailAdapter(this, title)
        recycler.adapter = adapter
        addButton.setOnClickListener {
            ARouter.getInstance().build(HomeRouter.ROUTER_ADD_CARE_DETAIL_ACTIVITY)
                    .withString("title", title)
                    .withString("theme", theme)
                    .withParcelable("petData", petData)
                    .navigation()
        }
    }

    private var request: GetCareDetailRequest? = null
    private fun getData() {
        request?.takeIf {
            !it.isFinished
        }?.apply {
            this.finish()
        }
        request = GetCareDetailRequest()
        request?.let {
            it.addUrlParam("username", AccountManager.getUserName())
            it.addUrlParam("petid", petData?.id)
            it.addUrlParam("theme", theme)
            it.setRequestListener(object : RequestListener<CareDetailModel> {
                override fun onComplete() {
                    pullToRefreshRecyclerView.onRefreshComplete()
                }

                override fun onSuccess(result: CareDetailModel?) {
                    result?.let {
                        if (it.success) {
                            if (result.info.isNullOrEmpty()) {
                                emptyText.text = "暂无数据"
                                emptyText.visibility = View.VISIBLE
                            } else {
                                updateView(result.info)
                            }
                        } else {
                            emptyText.text = "接口请求失败，请重试"
                            emptyText.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onError(e: Exception?) {
                    HandlerExceptionUtils.handleException(e)
                }

            })
        }
        HttpManager.addRequest(request)
    }

    private fun updateView(info: MutableList<CareDetailModel.CareDetailData>?) {
        adapter.dataList = info

        emptyText.visibility = View.GONE
    }

    private var deleteRequest: DeleteCareDetailRequest? = null
    fun deleteHistory(item: CareDetailModel.CareDetailData, position: Int) {
        showLoadingDialog()
        deleteRequest?.takeIf {
            !it.isFinished
        }?.apply {
            this.finish()
        }
        deleteRequest = DeleteCareDetailRequest()
        deleteRequest?.let {
            it.addUrlParam("username", AccountManager.getUserName())
            it.addUrlParam("id", item.id)
            it.addUrlParam("petid", item.petid)
            it.addUrlParam("theme", theme)
            it.setRequestListener(object : RequestListener<BaseModel> {
                override fun onComplete() {
                    dismissLoadingDialog()
                }

                override fun onSuccess(result: BaseModel?) {
                    result?.let {
                        if (it.success) {
                            val iterator = adapter.dataList?.iterator()
                            while (iterator?.hasNext() == true) {
                                val next = iterator.next()
                                if (next.id == item.id && next.petid == item.petid) {
                                    iterator.remove()
                                    adapter.notifyItemChanged(position)
                                    if (adapter.dataList.isNullOrEmpty()) {
                                        emptyText.visibility = View.VISIBLE
                                    }
                                    break
                                }
                            }
                            ToastUtils.showToast("删除成功")
                        } else {
                            ToastUtils.showToast(it.message)
                        }
                    }
                }

                override fun onError(e: Exception?) {
                    HandlerExceptionUtils.handleException(e)
                }

            })
        }
        HttpManager.addRequest(deleteRequest)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event : AddCareDetailEvent) {
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}