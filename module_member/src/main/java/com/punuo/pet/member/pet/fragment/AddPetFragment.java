package com.punuo.pet.member.pet.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.punuo.pet.member.R;
import com.punuo.pet.member.pet.model.RequestParam;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.httplib.upload.UploadPictureRequest;
import com.punuo.sys.sdk.httplib.upload.UploadResult;
import com.punuo.sys.sdk.util.FileUtil;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by han.chen.
 * Date on 2019-07-04.
 **/
public class AddPetFragment extends BaseFragment {

    public static AddPetFragment newInstance() {
        return new AddPetFragment();
    }

    private ImageView mUploadBtn;
    private RadioGroup mRadioGroup;
    private EditText mEditPetName;
    private EditText mEditPetType;
    private EditText mEditPetBirth;
    private EditText mEditPetWeight;
    private RequestParam mRequestParam;
    private String mPetAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_add_pet, container, false);
        initView();
        return mFragmentView;
    }

    private void initView() {
        mUploadBtn = mFragmentView.findViewById(R.id.upload_btn);
        mRadioGroup = mFragmentView.findViewById(R.id.radio_group);
        mEditPetName = mFragmentView.findViewById(R.id.edit_pet_name);
        mEditPetType = mFragmentView.findViewById(R.id.edit_pet_type);
        mEditPetBirth = mFragmentView.findViewById(R.id.edit_pet_birth);
        mEditPetWeight = mFragmentView.findViewById(R.id.edit_pet_weight);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(AddPetFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.SINGLE)
                        .imageFormat(PictureMimeType.JPEG)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    LocalMedia localMedia = selectList.get(0);
                    String path = localMedia.getPath();
                    if (isJPEG(path)) {
                        //TODO 上传图片
                        uploadPicture(compressBitmap(path));
                    } else {
                        ToastUtils.showToast("只支持jpg格式");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isJPEG(String path) {
        String type = PictureMimeType.getLastImgType(path);
        return type.equals(".jpg") || type.equals(".JPEG") || type.equals(".jpeg");
    }

    public RequestParam getRequestParam() {
        mRequestParam = new RequestParam();
        int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radio_dog) {
            mRequestParam.type = 1;
        } else if (checkedRadioButtonId == R.id.radio_cat) {
            mRequestParam.type = 2;
        }

        mRequestParam.photo = mPetAvatar;
        mRequestParam.name = mEditPetName.getText().toString().trim();
        mRequestParam.breed = 1;
        mRequestParam.date = mEditPetBirth.getText().toString().trim();
        mRequestParam.weight = mEditPetWeight.getText().toString().trim();
        return mRequestParam;
    }

    private UploadPictureRequest mUploadPictureRequest;
    private void uploadPicture(String path) {
        if (mUploadPictureRequest != null && !mUploadPictureRequest.isFinish()) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        showLoadingDialog("正在上传...");
        mUploadPictureRequest = new UploadPictureRequest();
        mUploadPictureRequest.addEntityParam("photo", file);
        mUploadPictureRequest.addEntityParam("userName", AccountManager.getUserInfo().userName);
        mUploadPictureRequest.setRequestListener(new RequestListener<UploadResult>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
                FileUtil.deleteTempDir();
            }

            @Override
            public void onSuccess(UploadResult result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    mPetAvatar = result.url;
                    Glide.with(PnApplication.getInstance()).load(result.url).into(mUploadBtn);
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(mUploadPictureRequest);
    }

    private String compressBitmap(String selectPath) {
        if (TextUtils.isEmpty(selectPath)) {
            return "";
        }
        showLoadingDialog("正在压缩图片...");
        Bitmap bitmap = FileUtil.compressBitmap(selectPath);
        String temp = FileUtil.saveBitmap(bitmap, String.valueOf(System.currentTimeMillis()));
        dismissLoadingDialog();
        return temp;
    }
}
