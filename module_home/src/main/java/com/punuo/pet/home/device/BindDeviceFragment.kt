package com.punuo.pet.home.device

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.DeviceInfoAdapter
import com.punuo.pet.home.device.event.AddBindCheckEvent
import com.punuo.pet.home.device.model.DeviceHost
import com.punuo.pet.home.device.model.DeviceInfo
import com.punuo.pet.home.device.model.DeviceModel
import com.punuo.pet.home.device.request.*
import com.punuo.sip.dev.BindDevSuccessEvent
import com.punuo.sip.dev.UnBindDevSuccessEvent
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.activity.QRScanActivity
import com.punuo.sys.sdk.fragment.BaseFragment
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.util.HandlerExceptionUtils
import com.punuo.sys.sdk.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.util.*

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
@RuntimePermissions
class BindDeviceFragment : BaseFragment() {
    companion object {
        const val QR_SCAN_REQUEST_CODE = 1
    }

    lateinit var pullToRefresh : PullToRefreshRecyclerView
    lateinit var mDeviceList: RecyclerView
    lateinit var mAddDevice: TextView
    lateinit var mTextEmpty: TextView

    private var mDeviceInfoAdapter: DeviceInfoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.home_bind_device_fragment, container, false)
        pullToRefresh = mFragmentView.findViewById(R.id.pull_to_refresh)
        mDeviceList = pullToRefresh.refreshableView
        mAddDevice = mFragmentView.findViewById(R.id.add_device)
        mTextEmpty = mFragmentView.findViewById(R.id.text_empty)
        initView()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        return mFragmentView
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(context)
        mDeviceList.layoutManager = layoutManager
        mDeviceInfoAdapter = DeviceInfoAdapter(context, ArrayList())
        mDeviceList.adapter = mDeviceInfoAdapter
        pullToRefresh.setOnRefreshListener {
            refresh()
        }
        refresh()
    }

    private var mGetBindDeviceRequest: GetBindDeviceRequest? = null

    /**
     * 获取绑定的设备信息
     */
    private fun getDeviceInfo() {
        mGetBindDeviceRequest?.takeIf {
            !it.isFinish
        }?.apply {
            this.finish()
        }
        mGetBindDeviceRequest = GetBindDeviceRequest()
        mGetBindDeviceRequest?.addUrlParam("username", AccountManager.getUserName())
        mGetBindDeviceRequest?.requestListener = object : RequestListener<DeviceModel?> {
            override fun onComplete() {
                pullToRefresh.onRefreshComplete()
            }

            override fun onSuccess(result: DeviceModel?) {
                if (result == null) {
                    return
                }
                updateView(result.mDeviceInfoList)
            }

            override fun onError(e: Exception) {}
        }
        HttpManager.addRequest(mGetBindDeviceRequest)
    }

    private var mBindDeviceRequest: BindDeviceRequest? = null

    /**
     * 绑定设备
     */
    private fun bindDevice(devId: String) {
        mBindDeviceRequest?.takeIf {
            !it.isFinish
        }?.apply {
            this.finish()
        }
        mBindDeviceRequest = BindDeviceRequest()
        mBindDeviceRequest?.addUrlParam("devid", devId)
        mBindDeviceRequest?.addUrlParam("username", AccountManager.getUserName())
        mBindDeviceRequest?.requestListener = object : RequestListener<BaseModel?> {
            override fun onComplete() {
                dismissLoadingDialog()
            }
            override fun onSuccess(result: BaseModel?) {
                if (result == null) {
                    return
                }
                ToastUtils.showToast(result.message)
                if (result.success) {
                    refresh()
                    EventBus.getDefault().post(BindDevSuccessEvent())
                }
            }

            override fun onError(e: java.lang.Exception) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        showLoadingDialog("设备绑定中...")
        HttpManager.addRequest(mBindDeviceRequest)
    }

    private var mJoinGroupRequest: JoinGroupRequest? = null

    /**
     * 申请加入已有的设备群组
     */
    private fun joinDevice(devId: String) {
        mJoinGroupRequest?.takeIf {
            !it.isFinish
        }?.apply {
            this.finish()
        }
        mJoinGroupRequest = JoinGroupRequest()
        mJoinGroupRequest?.addUrlParam("devid", devId)
        mJoinGroupRequest?.addUrlParam("username", AccountManager.getUserName())
        mJoinGroupRequest?.requestListener = object : RequestListener<BaseModel?> {
            override fun onComplete() {}
            override fun onSuccess(result: BaseModel?) {
                if (result == null) {
                    return
                }
                ToastUtils.showToast(result.message)
                if (result.success) {
                    refresh()
                    EventBus.getDefault().post(BindDevSuccessEvent())
                }
            }

            override fun onError(e: java.lang.Exception) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(mJoinGroupRequest)
    }

    /**
     * 提交审核信息
     */
    private fun addBindingCheck(devId: String) {
        val request = AddBindingCheckRequest()
        request.addUrlParam("devid", devId)
        request.addUrlParam("username", AccountManager.getUserName())
        request.requestListener = object : RequestListener<BaseModel?> {
            override fun onComplete() {

            }

            override fun onSuccess(result: BaseModel?) {
                result?.let {
                    if (result.success) {
                        ToastUtils.showToast("申请成功")
                        EventBus.getDefault().post(AddBindCheckEvent())
                    } else {
                        ToastUtils.showToast(it.message)
                    }
                }
            }

            override fun onError(e: java.lang.Exception?) {

            }

        }
        HttpManager.addRequest(request)
    }

    private var mCheckBindDeviceRequest: CheckBindDeviceRequest? = null

    private fun checkBindDevice(devId: String) {
        mCheckBindDeviceRequest?.takeIf {
            !it.isFinish
        }?.apply {
            this.finish()
        }
        mCheckBindDeviceRequest = CheckBindDeviceRequest()
        mCheckBindDeviceRequest?.addUrlParam("devid", devId)
        mCheckBindDeviceRequest?.requestListener = object : RequestListener<BaseModel?> {
            override fun onComplete() {}
            override fun onSuccess(result: BaseModel?) {
                if (result == null) {
                    return
                }
                if (result.success) {
                    bindDevice(devId)
                } else {
                    joinDevice(devId)
                }
            }

            override fun onError(e: java.lang.Exception) {}
        }
        HttpManager.addRequest(mCheckBindDeviceRequest)
    }

    /**
     * 检查是否设备存在主用户
     */
    private fun checkHost(devId: String) {
        val request = CheckDeviceHostRequest()
        request.addUrlParam("devid", devId)
        request.requestListener = object : RequestListener<DeviceHost?> {
            override fun onComplete() {
            }

            override fun onSuccess(result: DeviceHost?) {
                result?.data?.let {
                    if (it.exist) {
                        //存在主用户
                        showHasHostDialog(devId)
                    } else {
                        //不存在主用户
                        showNoHostDialog(devId)
                    }
                }
            }

            override fun onError(e: java.lang.Exception?) {
            }

        }
        HttpManager.addRequest(request)
    }

    /**
     * 无主用户不存在提醒
     */
    private fun showNoHostDialog(devId: String) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle("温馨提醒")
                .setMessage("当前要绑定的设备暂无主用户，确认绑定之后，您自动成为主用户，是否确认绑定？")
                .setPositiveButton("绑定") { dialog, which ->
                    dialog.dismiss()
                    bindDevice(devId)
                    activity?.finish()
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }.create()
        alertDialog.show()
    }

    /**
     * 主用户存在提醒
     */
    private fun showHasHostDialog(devId: String) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle("温馨提醒")
                .setMessage("当前要绑定的设备已存在主用户，需要向主用户申请，是否确定申请绑定？")
                .setPositiveButton("申请") { dialog, which ->
                    dialog.dismiss()
                    addBindingCheck(devId)
                    activity?.finish()
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }.create()
        alertDialog.show()
    }

    private fun updateView(deviceInfoList: List<DeviceInfo>?) {
        if (deviceInfoList == null || deviceInfoList.isEmpty()) {
            mTextEmpty.visibility = View.VISIBLE
            mDeviceList.visibility = View.GONE
//            mAddDevice.visibility = View.VISIBLE
            mAddDevice.setOnClickListener {
                show()
            }
        } else {
            mDeviceList.visibility = View.VISIBLE
            mTextEmpty.visibility = View.GONE
//        mAddDevice.visibility = View.GONE
            mDeviceInfoAdapter?.appendData(deviceInfoList)
            mAddDevice.setOnClickListener {
                ToastUtils.showToast("暂时只支持绑定一台设备，如若更换绑定设备，请先解绑")
            }
        }
    }

    private var dialog: AlertDialog? = null
    private fun show() {
        context?.let {
            dialog?.dismiss()
            val layout = LayoutInflater.from(it).inflate(R.layout.home_dialog_layout, null)
            val scanCode = layout.findViewById<TextView>(R.id.tv_saoma)
            val input = layout.findViewById<TextView>(R.id.tv_shoudong)
            val cancel = layout.findViewById<ImageView>(R.id.cancel)
            scanCode.setOnClickListener {
                openScanWithPermissionCheck()
                dialog?.dismiss()
            }
            input.setOnClickListener {
                val editText = EditText(context)
                AlertDialog.Builder(context)
                        .setTitle("请输入设备号")
                        .setView(editText)
                        .setPositiveButton("确定") { dialogInterface, i ->
                            val devId = editText.text.toString()
                            checkHost(devId)
                            dialog?.dismiss()
                        }.setNegativeButton("取消", DialogInterface.OnClickListener { dialogInterface, i ->
                            return@OnClickListener
                        }).show()
                dialog?.dismiss()
            }
            cancel.setOnClickListener {
                dialog?.dismiss()
            }
            dialog = AlertDialog.Builder(context)
                    .setView(layout)
                    .create()
            dialog?.show()
        }

    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun openScan() {
        val intent = Intent(context, QRScanActivity::class.java)
        startActivityForResult(intent, QR_SCAN_REQUEST_CODE)
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun openScanError() {
        ToastUtils.showToast("权限获取失败")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QR_SCAN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val result = data.getStringExtra("result")
                    checkHost(result)
                }
            }
        }
    }

    fun refresh() {
        getDeviceInfo()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnBindDevSuccessEvent) {
        refresh()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}