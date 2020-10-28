package com.punuo.pet.member.user

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.punuo.pet.member.R
import com.punuo.pet.member.pet.model.InfoData
import com.punuo.pet.member.pet.request.AddUserInfoRequest
import com.punuo.pet.member.pet.request.UpdateUserInfoRequest
import com.punuo.pet.router.MemberRouter
import com.punuo.sys.sdk.PnApplication
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.account.UserManager
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.httplib.upload.UploadPictureRequest
import com.punuo.sys.sdk.httplib.upload.UploadResult
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.util.BitmapUtil
import com.punuo.sys.sdk.util.FileUtil
import com.punuo.sys.sdk.util.HandlerExceptionUtils
import com.punuo.sys.sdk.util.ToastUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by han.chen.
 * Date on 2020/10/28.
 **/
@Route(path = MemberRouter.ROUTER_USER_INFO_ACTIVITY)
class UserInfoActivity :BaseSwipeBackActivity() {

    @JvmField
    @Autowired(name = "canEdit")
    var canEdit: Boolean = true

    private lateinit var titleText: TextView
    private lateinit var backIcon: View

    private lateinit var userAvatar: ImageView
    private lateinit var userNick: TextView
    private lateinit var userSex: TextView
    private lateinit var userBirth: TextView

    private var mUserAvatar: String? = null
    private var mUserNick: String? = null
    private var mUserSex: Int = -1
    private var mUserBirth: String? = null

    private lateinit var submit: TextView

    private val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        ARouter.getInstance().inject(this)
        initView()
    }
    private fun initView() {
        titleText = findViewById(R.id.title) as TextView
        titleText.text = if (TextUtils.isEmpty(AccountManager.getUserInfo().userName)) "添加用户信息" else if (canEdit) "编辑用户信息" else "查看用户信息"
        backIcon = findViewById(R.id.back)
        backIcon.setOnClickListener {
            onBackPressed()
        }

        userAvatar = findViewById(R.id.user_avatar) as ImageView
        userNick = findViewById(R.id.user_name) as TextView
        userSex = findViewById(R.id.user_sex) as TextView
        userBirth = findViewById(R.id.user_birth) as TextView

        takeIf {
            !TextUtils.isEmpty(AccountManager.getUserInfo().userName)
        }?.let {
            val userInfo = AccountManager.getUserInfo()
            Glide.with(this).load(userInfo.avatar).into(userAvatar)
            mUserAvatar = userInfo.avatar

            userNick.text = userInfo.nickName
            mUserNick = userInfo.nickName

            userBirth.text = userInfo.getBirth()
            mUserBirth = userInfo.getBirth()

            userSex.text = if (userInfo.gender == 1) "男" else "女"
            mUserSex = userInfo.gender
        }

        if (canEdit) {
            userAvatar.setOnClickListener {
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.SINGLE)
                        .imageFormat(PictureMimeType.JPEG)
                        .forResult(PictureConfig.CHOOSE_REQUEST)
            }
            userNick.setOnClickListener {
                val editText = EditText(this)
                editText.hint = "请输入用户昵称"
                mUserNick?.let { text->
                    editText.setText(text)
                    editText.setSelection(text.length)
                }
                AlertDialog.Builder(this)
                        .setTitle("请输入用户昵称")
                        .setView(editText)
                        .setPositiveButton("确定") { dialog: DialogInterface, which: Int ->
                            val text = editText.text.toString()
                            if (TextUtils.isEmpty(text)) {
                                ToastUtils.showToast("请输入用户昵称")
                                return@setPositiveButton
                            }
                            mUserNick = text
                            userNick.text = text
                            dialog.dismiss()
                        }
                        .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }.show()
            }
            userSex.setOnClickListener {
                val pvOption = OptionsPickerBuilder(this,
                        OnOptionsSelectListener { options1, options2, options3, v ->
                            mUserSex = options1 + 1
                            userSex.text = InfoData.getUserSexList()[options1]
                        })
                        .setTitleText("请选择用户性别")
                        .build<String>()
                pvOption.setPicker(InfoData.getUserSexList())
                pvOption.show()
            }
            userBirth.setOnClickListener {
                TimePickerBuilder(this, OnTimeSelectListener { date: Date?, v1: View? ->
                    val select = Calendar.getInstance()
                    select.time = date
                    if (select.after(Calendar.getInstance())) {
                        ToastUtils.showToast("当前选择是未来时间，请重新设置用户生日")
                        return@OnTimeSelectListener
                    }
                    mUserBirth = mSimpleDateFormat.format(date)
                    userBirth.text = mSimpleDateFormat.format(date)
                })
                        .setType(booleanArrayOf(true, true, true, false, false, false))
                        .build().show()
            }
        }
        submit = findViewById(R.id.submit) as TextView
        submit.text = if (TextUtils.isEmpty(AccountManager.getUserInfo().userName)) "提交" else "修改"
        submit.visibility = if (canEdit) View.VISIBLE else View.GONE

        submit.setOnClickListener {
            if (checkValid()) {
                if (!TextUtils.isEmpty(AccountManager.getUserInfo().userName)) {
                    updateUserInfo()
                } else {
                    addUserInfo()
                }
            }
        }
    }

    private fun checkValid() :Boolean {
        if (TextUtils.isEmpty(mUserAvatar)) {
            ToastUtils.showToast("请上传用户头像")
            return false
        }

        if (TextUtils.isEmpty(mUserNick)) {
            ToastUtils.showToast("请输入用户名字")
            return false
        }

        if (mUserSex == -1) {
            ToastUtils.showToast("请选择用户性别")
            return false
        }

        if (TextUtils.isEmpty(mUserBirth)) {
            ToastUtils.showToast("请输入用户生日")
            return false
        }

        return true
    }

    private var mUpdateUserInfoRequest : UpdateUserInfoRequest? = null

    private fun updateUserInfo () {
        if (mUpdateUserInfoRequest != null && !mUpdateUserInfoRequest!!.isFinish) {
            return
        }
        mUpdateUserInfoRequest = UpdateUserInfoRequest()
        mUpdateUserInfoRequest?.addUrlParam("userName", AccountManager.getUserName())
        mUpdateUserInfoRequest?.addUrlParam("photo", mUserAvatar)
        mUpdateUserInfoRequest?.addUrlParam("birth", mUserBirth)
        mUpdateUserInfoRequest?.addUrlParam("nickName", mUserNick)
        mUpdateUserInfoRequest?.addUrlParam("gender", mUserSex)
        mUpdateUserInfoRequest?.requestListener = object : RequestListener<BaseModel?> {
            override fun onComplete() {}
            override fun onSuccess(result: BaseModel?) {
                if (result == null) {
                    return
                }
                if (result.success) {
                    UserManager.getUserInfo(AccountManager.getUserName())
                    finish()
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message)
                }
            }

            override fun onError(e: java.lang.Exception) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(mUpdateUserInfoRequest)
    }

    private var mAddUserInfoRequest: AddUserInfoRequest? = null

    private fun addUserInfo() {
        if (mAddUserInfoRequest != null && !mAddUserInfoRequest!!.isFinish) {
            return
        }
        mAddUserInfoRequest = AddUserInfoRequest()
        mAddUserInfoRequest?.addUrlParam("userName", AccountManager.getUserName())
        mAddUserInfoRequest?.addUrlParam("photo", mUserAvatar)
        mAddUserInfoRequest?.addUrlParam("birth", mUserBirth)
        mAddUserInfoRequest?.addUrlParam("nickName", mUserNick)
        mAddUserInfoRequest?.addUrlParam("gender", mUserSex)
        mAddUserInfoRequest?.requestListener = object : RequestListener<BaseModel?> {
            override fun onComplete() {}
            override fun onSuccess(result: BaseModel?) {
                if (result == null) {
                    return
                }
                if (result.success) {
                    UserManager.getUserInfo(AccountManager.getUserName())
                    finish()
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message)
                }
            }

            override fun onError(e: java.lang.Exception) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(mAddUserInfoRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    val localMedia = selectList[0]
                    val path = localMedia.path
                    if (BitmapUtil.isJPEG(path)) {
                        uploadPicture(compressBitmap(path))
                    } else {
                        ToastUtils.showToast("只支持jpg格式")
                    }
                }
                else -> {
                }
            }
        }
    }

    private var mUploadPictureRequest: UploadPictureRequest? = null

    private fun uploadPicture(path: String) {
        if (mUploadPictureRequest != null && !mUploadPictureRequest!!.isFinish) {
            return
        }
        val file = File(path)
        if (!file.exists()) {
            return
        }
        showLoadingDialog("正在上传...")
        mUploadPictureRequest = UploadPictureRequest()
        mUploadPictureRequest!!.addEntityParam("photo", file)
        mUploadPictureRequest!!.addEntityParam("userName", AccountManager.getUserName())
        mUploadPictureRequest!!.requestListener = object : RequestListener<UploadResult?> {
            override fun onComplete() {
                dismissLoadingDialog()
                FileUtil.deleteTempDir()
            }

            override fun onSuccess(result: UploadResult?) {
                if (result == null) {
                    return
                }
                if (result.success) {
                    mUserAvatar = result.url
                    Glide.with(PnApplication.getInstance()).load(result.url).into(userAvatar)
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message)
                }
            }

            override fun onError(e: Exception) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(mUploadPictureRequest)
    }

    private fun compressBitmap(selectPath: String): String {
        if (TextUtils.isEmpty(selectPath)) {
            return ""
        }
        showLoadingDialog("正在压缩图片...")
        val bitmap = FileUtil.compressBitmap(selectPath)
        val temp = FileUtil.saveBitmap(bitmap, System.currentTimeMillis().toString())
        dismissLoadingDialog()
        return temp
    }
}