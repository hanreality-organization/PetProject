package com.punuo.pet.member.pet

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
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
import com.punuo.pet.event.AddPetEvent
import com.punuo.pet.event.UpdatePetEvent
import com.punuo.pet.member.R
import com.punuo.pet.member.pet.model.InfoData
import com.punuo.pet.member.pet.request.AddPetRequest
import com.punuo.pet.member.pet.request.UpdatePetRequest
import com.punuo.pet.model.PetData
import com.punuo.pet.router.MemberRouter
import com.punuo.sys.sdk.PnApplication
import com.punuo.sys.sdk.account.AccountManager
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
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by han.chen.
 * Date on 2020/10/27.
 **/
@Route(path = MemberRouter.ROUTER_PET_INFO_ACTIVITY)
class PetInfoActivity : BaseSwipeBackActivity() {

    @JvmField
    @Autowired(name = "petData")
    var petData: PetData? = null

    @JvmField
    @Autowired(name = "canEdit")
    var canEdit: Boolean = true

    private lateinit var titleText: TextView
    private lateinit var backIcon: View

    private lateinit var petAvatar: ImageView
    private lateinit var petName: TextView
    private lateinit var petType: TextView
    private lateinit var petSex: TextView
    private lateinit var petBirth: TextView
    private lateinit var petWeight: TextView

    private lateinit var submit: TextView

    private var mPetAvatar: String? = null
    private var mPetName: String? = null
    private var mPetType: Int = -1
    private var mPetSex: Int = -1
    private var mPetBirth: String? = null
    private var mPetWeight: String? = null

    private val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_info)
        ARouter.getInstance().inject(this)
        initView()
    }

    private fun initView() {
        titleText = findViewById(R.id.title) as TextView
        titleText.text = if (petData == null) "添加宠物" else if (canEdit) "编辑宠物" else "查看宠物"
        backIcon = findViewById(R.id.back)
        backIcon.setOnClickListener {
            onBackPressed()
        }

        petAvatar = findViewById(R.id.pet_avatar) as ImageView
        petName = findViewById(R.id.pet_name) as TextView
        petType = findViewById(R.id.pet_type) as TextView
        petSex = findViewById(R.id.pet_sex) as TextView
        petBirth = findViewById(R.id.pet_birth) as TextView
        petWeight = findViewById(R.id.pet_weight) as TextView

        submit = findViewById(R.id.submit) as TextView

        petData?.let {
            Glide.with(this).load(it.avatar).into(petAvatar)
            mPetAvatar = it.avatar

            petName.text = it.petname
            mPetName = it.petname

            petBirth.text = it.getBirth()
            mPetBirth = it.getBirth()

            petSex.text = if (it.sex == 1) "公" else "母"
            mPetSex = it.sex

            petType.text = when (it.breed) {
                1 -> "英国短毛猫"
                2 -> "美国短毛猫"
                3 -> "异国短毛猫"
                else -> "其他"
            }
            mPetType = it.breed

            petWeight.text = "${it.weight}kg"
            mPetWeight = "${it.weight}"
        }

        if (canEdit) {
            petAvatar.setOnClickListener {
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.SINGLE)
                        .imageFormat(PictureMimeType.JPEG)
                        .forResult(PictureConfig.CHOOSE_REQUEST)
            }
            petName.setOnClickListener {
                val editText = EditText(this)
                editText.hint = "请输入宠物名字"
                mPetName?.let {text->
                    editText.setText(text)
                    editText.setSelection(text.length)
                }
                AlertDialog.Builder(this)
                        .setTitle("请输入宠物名字")
                        .setView(editText)
                        .setPositiveButton("确定") { dialog: DialogInterface, which: Int ->
                            val text = editText.text.toString()
                            if (TextUtils.isEmpty(text)) {
                                ToastUtils.showToast("请输入宠物名字")
                                return@setPositiveButton
                            }
                            mPetName = text
                            petName.text = text
                            dialog.dismiss()
                        }
                        .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }.show()
            }
            petType.setOnClickListener {
                val pvOption = OptionsPickerBuilder(this,
                        OnOptionsSelectListener { options1, options2, options3, v ->
                            mPetType = options1 + 1
                            petType.text = InfoData.getPetTypeList()[options1]
                        })
                        .setTitleText("请选择宠物品种")
                        .build<String>()
                pvOption.setPicker(InfoData.getPetTypeList())
                pvOption.show()
            }
            petSex.setOnClickListener {
                val pvOption = OptionsPickerBuilder(this,
                        OnOptionsSelectListener { options1, options2, options3, v ->
                            mPetSex = options1 + 1
                            petSex.text = InfoData.getPetSexList()[options1]
                        })
                        .setTitleText("请选择宠物性别")
                        .build<String>()
                pvOption.setPicker(InfoData.getPetSexList())
                pvOption.show()
            }
            petBirth.setOnClickListener {
                TimePickerBuilder(this, OnTimeSelectListener { date: Date?, v1: View? ->
                    val select = Calendar.getInstance()
                    select.time = date
                    if (select.after(Calendar.getInstance())) {
                        ToastUtils.showToast("当前选择是未来时间，请重新设置宠物生日")
                        return@OnTimeSelectListener
                    }
                    mPetBirth = mSimpleDateFormat.format(date)
                    petBirth.text = mSimpleDateFormat.format(date)
                })
                        .setType(booleanArrayOf(true, true, true, false, false, false))
                        .build().show()

            }
            petWeight.setOnClickListener {
                val editText = EditText(this)
                editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                editText.hint = "请输入宠物体重"
                mPetWeight?.let {text->
                    editText.setText(text)
                    editText.setSelection(text.length)
                }
                AlertDialog.Builder(this)
                        .setTitle("请输入宠物体重")
                        .setView(editText)
                        .setPositiveButton("确定") { dialog: DialogInterface, which: Int ->
                            val text = editText.text.toString()
                            if (TextUtils.isEmpty(text)) {
                                ToastUtils.showToast("请输入宠物体重")
                                return@setPositiveButton
                            }
                            mPetWeight = text
                            petWeight.text = "${text}kg"
                            dialog.dismiss()
                        }
                        .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }.show()
            }
        }

        submit.text = if (petData == null) "提交" else "修改"
        submit.visibility = if (canEdit) View.VISIBLE else View.GONE

        submit.setOnClickListener {
            if (checkValid()) {
                if (petData != null) {
                    updatePetInfo()
                } else {
                    addPetInfo()
                }
            }
        }

    }

    private fun checkValid() :Boolean {
        if (TextUtils.isEmpty(mPetAvatar)) {
            ToastUtils.showToast("请上传宠物头像")
            return false
        }

        if (TextUtils.isEmpty(mPetName)) {
            ToastUtils.showToast("请输入宠物名字")
            return false
        }

        if (mPetType == -1) {
            ToastUtils.showToast("请选择宠物品种")
            return false
        }

        if (mPetSex == -1) {
            ToastUtils.showToast("请选择宠物性别")
            return false
        }

        if (TextUtils.isEmpty(mPetBirth)) {
            ToastUtils.showToast("请输入宠物生日")
            return false
        }

        if (TextUtils.isEmpty(mPetWeight)) {
            ToastUtils.showToast("请输入宠物体重")
            return false
        }
        return true
    }

    private var mUpdatePetRequest : UpdatePetRequest? = null

    private fun updatePetInfo() {
        if (mUpdatePetRequest != null && !mUpdatePetRequest!!.isFinish) {
            return
        }
        mUpdatePetRequest = UpdatePetRequest()
        mUpdatePetRequest?.addUrlParam("id", petData?.id)
        mUpdatePetRequest?.addUrlParam("sex",mPetSex)
        mUpdatePetRequest?.addUrlParam("photo", mPetAvatar)
        mUpdatePetRequest?.addUrlParam("petName", mPetName)
        mUpdatePetRequest?.addUrlParam("breed", mPetType)
        mUpdatePetRequest?.addUrlParam("birth", mPetBirth)
        mUpdatePetRequest?.addUrlParam("weight", mPetWeight)
        mUpdatePetRequest?.addUrlParam("unit", 1)
        mUpdatePetRequest?.addUrlParam("userName", AccountManager.getUserName())
        mUpdatePetRequest?.requestListener = object :RequestListener<BaseModel> {
            override fun onComplete() {
            }

            override fun onSuccess(result: BaseModel?) {
                result?.let {
                    ToastUtils.showToast(it.message)
                    if (it.success) {
                        EventBus.getDefault().post(UpdatePetEvent())
                        finish()
                    }
                }
            }

            override fun onError(e: java.lang.Exception?) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(mUpdatePetRequest)
    }

    private var mAddPetRequest: AddPetRequest? = null
    private fun addPetInfo() {
        if (mAddPetRequest != null && !mAddPetRequest!!.isFinish) {
            return
        }
        mAddPetRequest = AddPetRequest()
        mAddPetRequest?.addUrlParam("sex",mPetSex)
        mAddPetRequest?.addUrlParam("photo", mPetAvatar)
        mAddPetRequest?.addUrlParam("petName", mPetName)
        mAddPetRequest?.addUrlParam("breed", mPetType)
        mAddPetRequest?.addUrlParam("birth", mPetBirth)
        mAddPetRequest?.addUrlParam("weight", mPetWeight)
        mAddPetRequest?.addUrlParam("unit", 1)
        mAddPetRequest?.addUrlParam("userName", AccountManager.getUserName())
        mAddPetRequest?.requestListener = object :RequestListener<BaseModel> {
            override fun onComplete() {
            }

            override fun onSuccess(result: BaseModel?) {
                result?.let {
                    if (it.success) {
                        EventBus.getDefault().post(AddPetEvent())
                        if (!TextUtils.isEmpty(AccountManager.getUserInfo().nickName)) {
                            finish()
                        } else {
                            ARouter.getInstance().build(MemberRouter.ROUTER_USER_INFO_ACTIVITY)
                                    .withBoolean("canEdit", true)
                                    .navigation()
                        }
                    }
                    ToastUtils.showToast(it.message)
                }
            }

            override fun onError(e: java.lang.Exception?) {
                HandlerExceptionUtils.handleException(e)
            }

        }
        HttpManager.addRequest(mAddPetRequest)
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
                    mPetAvatar = result.url
                    Glide.with(PnApplication.getInstance()).load(result.url).into(petAvatar)
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