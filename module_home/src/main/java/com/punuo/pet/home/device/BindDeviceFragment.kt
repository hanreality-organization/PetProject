package com.punuo.pet.home.device

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.DeviceInfoAdapter
import com.punuo.pet.home.device.model.DeviceInfo
import com.punuo.pet.home.device.model.DeviceModel
import com.punuo.pet.home.device.request.BindDeviceRequest
import com.punuo.pet.home.device.request.CheckBindDeviceRequest
import com.punuo.pet.home.device.request.GetBindDeviceRequest
import com.punuo.pet.home.device.request.JoinGroupRequest
import com.punuo.pet.router.SDKRouter
import com.punuo.sip.dev.BindDevSuccessEvent
import com.punuo.sip.dev.UnBindDevSuccessEvent
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.activity.QRScanActivity
import com.punuo.sys.sdk.fragment.BaseFragment
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.util.CommonUtil
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

    lateinit var mDeviceList: RecyclerView
    lateinit var mAddDevice: TextView
    lateinit var mTextEmpty: TextView

    private var mDeviceInfoAdapter: DeviceInfoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.home_bind_device_fragment, container, false)
        mDeviceList = mFragmentView.findViewById(R.id.device_list)
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
        refresh()
        mAddDevice.setOnClickListener {
            show()
        }
    }

    private var mGetBindDeviceRequest: GetBindDeviceRequest? = null

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
                dismissLoadingDialog()
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
        HttpManager.addRequest(mBindDeviceRequest)
    }

    private var mJoinGroupRequest: JoinGroupRequest? = null

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

    private fun updateView(deviceInfoList: List<DeviceInfo>?) {
        if (deviceInfoList == null || deviceInfoList.isEmpty()) {
            mTextEmpty.visibility = View.VISIBLE
            mDeviceList.visibility = View.GONE
            mAddDevice.visibility = View.VISIBLE
            return
        }
        mDeviceList.visibility = View.VISIBLE
        mTextEmpty.visibility = View.GONE
        mAddDevice.visibility = View.GONE
        mDeviceInfoAdapter?.appendData(deviceInfoList)
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
                            checkBindDevice(devId)
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
                    showLoadingDialog("绑定设备中...")
                    checkBindDevice(result)
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