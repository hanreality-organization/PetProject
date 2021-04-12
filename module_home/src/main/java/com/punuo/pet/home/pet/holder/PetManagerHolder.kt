package com.punuo.pet.home.pet.holder

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.punuo.pet.event.DelPetEvent
import com.punuo.pet.home.R
import com.punuo.pet.home.pet.request.DeletePetRequest
import com.punuo.pet.model.PetData
import com.punuo.pet.router.MemberRouter
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.activity.BaseActivity
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.recyclerview.BaseViewHolder
import com.punuo.sys.sdk.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

/**
 * Created by han.chen.
 * Date on 2020/10/23.
 **/
class PetManagerHolder(var view: View) : BaseViewHolder<PetData>(view) {
    private val petAvatar = view.findViewById<ImageView>(R.id.pet_avatar)
    private val petName = view.findViewById<TextView>(R.id.pet_name)
    private val delete = view.findViewById<View>(R.id.delete)
    private val edit = view.findViewById<View>(R.id.edit)
    override fun bindData(t: PetData?, position: Int) {
        t?.let {petData->
            Glide.with(view.context).load(petData.avatar).into(petAvatar)
            petName.text = petData.petname
            delete.setOnClickListener {
                showDelConfirmDialog(t)
            }
            edit.setOnClickListener {
                ARouter.getInstance().build(MemberRouter.ROUTER_PET_INFO_ACTIVITY)
                        .withParcelable("petData", petData)
                        .withBoolean("canEdit", true)
                        .navigation()
            }
            itemView.setOnClickListener {
                ARouter.getInstance().build(MemberRouter.ROUTER_PET_INFO_ACTIVITY)
                        .withParcelable("petData", petData)
                        .withBoolean("canEdit", false)
                        .navigation()
            }
        }
    }
    private fun showDelConfirmDialog(t: PetData) {
        val dialog = AlertDialog.Builder(view.context)
                .setTitle(view.context.getString(R.string.string_attention))
                .setMessage(view.context.getString(R.string.string_delete_pet_tip))
                .setPositiveButton(view.context.getString(R.string.string_confirm)) { dialog, which ->
                    deletePet(t)
                    dialog.dismiss()
                }
                .setNegativeButton(view.context.getString(R.string.string_cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
        dialog.show()
    }
    private var request : DeletePetRequest? = null

    private fun deletePet(t: PetData) {
        request?.takeIf {
            !it.isFinish
        }?.apply {
            this.finish()
        }
        (view.context as? BaseActivity)?.showLoadingDialog()
        request = DeletePetRequest()
        request?.addUrlParam("username", AccountManager.getUserName())
        request?.addUrlParam("id", t.id)
        request?.requestListener = object :RequestListener<BaseModel?> {
            override fun onComplete() {
                (view.context as? BaseActivity)?.dismissLoadingDialog()
            }

            override fun onSuccess(result: BaseModel?) {
                result?.let {
                    if (it.success) {
                        EventBus.getDefault().post(DelPetEvent())
                    }
                    ToastUtils.showToast(it.message)
                }
            }

            override fun onError(e: Exception?) {
            }
        }
        HttpManager.addRequest(request)
    }
}