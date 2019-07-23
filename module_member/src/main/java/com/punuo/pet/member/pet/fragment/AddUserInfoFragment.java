package com.punuo.pet.member.pet.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.punuo.pet.member.R;
import com.punuo.pet.member.pet.model.UserParam;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.httplib.upload.UploadPictureRequest;
import com.punuo.sys.sdk.httplib.upload.UploadResult;
import com.punuo.sys.sdk.util.BitmapUtil;
import com.punuo.sys.sdk.util.FileUtil;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by han.chen.
 * Date on 2019-07-04.
 **/
public class AddUserInfoFragment extends BaseFragment {

    public static AddUserInfoFragment newInstance() {
        return new AddUserInfoFragment();
    }
    private ImageView mUploadView;
    private EditText mNickEdit;
    private RadioGroup mGenderSelect;
    private TextView mBirthText;
    private String mUserAvatar;
    private DatePickerDialog mDatePickerDialog;
    private Activity mActivity;
    private UserParam mUserParam = new UserParam();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mFragmentView = inflater.inflate(R.layout.fragment_add_user, container, false);
        initView();
        return mFragmentView;
    }

    private void initView() {
        mUploadView = mFragmentView.findViewById(R.id.upload_btn);
        mNickEdit = mFragmentView.findViewById(R.id.edit_user_nick);
        mGenderSelect = mFragmentView.findViewById(R.id.radio_group);
        mBirthText = mFragmentView.findViewById(R.id.edit_user_birth);

        mUploadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(AddUserInfoFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.SINGLE)
                        .imageFormat(PictureMimeType.JPEG)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        mBirthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
                    return;
                }
                Calendar calendar = Calendar.getInstance(); //Calendar类：可以理解为日期
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                mDatePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = getResources().getString(R.string.date, year, month + 1, dayOfMonth);
                        mBirthText.setText(date);
                    }
                }, yy, mm, dd);
                mDatePickerDialog.show();
            }
        });
    }

    public UserParam getUserParam() {
        int checkedRadioButtonId = mGenderSelect.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radio_male) {
            mUserParam.gender = 1;
        } else if (checkedRadioButtonId == R.id.radio_female) {
            mUserParam.gender = 2;
        }

        mUserParam.avatar = mUserAvatar;
        mUserParam.nickName = mNickEdit.getText().toString().trim();
        mUserParam.birth = mBirthText.getText().toString().trim();

        return mUserParam;
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
                    if (BitmapUtil.isJPEG(path)) {
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
        mUploadPictureRequest.addEntityParam("userName", AccountManager.getUserName());
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
                    mUserAvatar = result.url;
                    Glide.with(PnApplication.getInstance()).load(result.url).into(mUploadView);
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
