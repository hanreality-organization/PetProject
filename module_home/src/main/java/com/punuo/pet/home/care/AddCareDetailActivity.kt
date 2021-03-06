package com.punuo.pet.home.care

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.punuo.pet.home.R
import com.punuo.pet.home.care.event.AddCareDetailEvent
import com.punuo.pet.home.care.model.CareDetailModel
import com.punuo.pet.home.care.request.SubmitCareDetailRequest
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
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by han.chen.
 * Date on 2020/12/9.
 **/
@Route(path = HomeRouter.ROUTER_ADD_CARE_DETAIL_ACTIVITY)
class AddCareDetailActivity : BaseSwipeBackActivity() {

    @JvmField
    @Autowired(name = "title")
    var title: String? = null

    @JvmField
    @Autowired(name = "theme")
    var theme: String? = null

    @JvmField
    @Autowired(name = "petData")
    var petData: PetData? = null

    @JvmField
    @Autowired(name = "careDetail")
    var careDetail : CareDetailModel.CareDetailData? = null

    private lateinit var titleView: TextView
    private lateinit var backView: View
    private lateinit var descEdit : EditText
    private lateinit var timePicker : TextView
    private lateinit var submitButton : View

    private val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var mFinishTime :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_care_detail_layout)
        ARouter.getInstance().inject(this)
        initView()
    }

    private fun initView() {
        titleView = findViewById(R.id.title) as TextView
        val defaultTitle = title?: getString(R.string.string_daily_care)
        titleView.text = getString(R.string.string_add_item, defaultTitle)

        backView = findViewById(R.id.back)
        backView.setOnClickListener {
            scrollToFinishActivity()
        }

        val canEdit = careDetail == null
        descEdit = findViewById(R.id.desc_input) as EditText
        timePicker = findViewById(R.id.time_picker) as TextView
        submitButton = findViewById(R.id.submit_button)
        descEdit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(300))
        descEdit.isEnabled = canEdit
        timePicker.isEnabled = canEdit
        timePicker.setOnClickListener {
            hideKeyboard(this)
            TimePickerBuilder(this, OnTimeSelectListener { date: Date?, v1: View? ->
                val select = Calendar.getInstance()
                select.time = date
                if (select.after(Calendar.getInstance())) {
                    ToastUtils.showToast(getString(R.string.string_time_choose_tip))
                    return@OnTimeSelectListener
                }
                mFinishTime = mSimpleDateFormat.format(date)
                timePicker.text = mSimpleDateFormat.format(date)
            })
                    .setType(booleanArrayOf(true, true, true, true, true, true))
                    .build().show()
        }
        if (canEdit) {
            submitButton.visibility = View.VISIBLE
            submitButton.setOnClickListener {
                if (checkValid()) {
                    submit()
                }
            }
        } else {
            submitButton.visibility = View.GONE
        }

        careDetail?.let {
            descEdit.setText(it.detail)
            timePicker.text = it.time
        }
    }

    private fun checkValid() :Boolean {
        if (TextUtils.isEmpty(descEdit.text.trim())) {
            ToastUtils.showToast(getString(R.string.string_desc))
            return false
        }
        if (TextUtils.isEmpty(mFinishTime)) {
            ToastUtils.showToast(getString(R.string.string_toast_time))
            return false
        }
        return true
    }

    private var submitRequest :SubmitCareDetailRequest? = null
    private fun submit() {
        showLoadingDialog()
        submitRequest?.takeIf {
            !it.isFinished
        }?.apply {
            this.finish()
        }
        submitRequest = SubmitCareDetailRequest()
        submitRequest?.let {
            it.addUrlParam("username", AccountManager.getUserName())
            it.addUrlParam("detail", descEdit.text.trim())
            it.addUrlParam("petid", petData?.id)
            it.addUrlParam("time", mFinishTime)
            it.addUrlParam("theme", theme)
            it.setRequestListener(object : RequestListener<BaseModel> {
                override fun onComplete() {
                    dismissLoadingDialog()
                }

                override fun onSuccess(result: BaseModel?) {
                    result?.let {
                        if (it.success) {
                            ToastUtils.showToast(getString(R.string.string_add_success))
                            EventBus.getDefault().post(AddCareDetailEvent())
                            finish()
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
        HttpManager.addRequest(submitRequest)
    }

    fun hideKeyboard(act: Activity?) {
        if (act != null && act.currentFocus != null) {
            val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(act.currentFocus.windowToken, 0)
        }
    }
}