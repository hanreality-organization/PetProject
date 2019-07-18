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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private Spinner mEditPetType;
    private TextView mEditPetBirth;
    private EditText mEditPetWeight;
    private Spinner mEditPetWeightUnit;
    private RequestParam mRequestParam = new RequestParam();
    private String mPetAvatar;
    private int mPetType;
    private int mUnit;

    private ArrayAdapter<String> mPetAdapter;
    private ArrayAdapter<String> mUnitAdapter;
    private Activity mActivity;
    private DatePickerDialog mDatePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
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
        mEditPetWeightUnit = mFragmentView.findViewById(R.id.edit_pet_weight_unit);

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

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setSpinnerArray(checkedId);
            }
        });
        setSpinnerArray(mRadioGroup.getCheckedRadioButtonId());

        mEditPetBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
                    return;
                }
                Calendar calendar = Calendar.getInstance();//Calendar类：可以理解为日期
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                mDatePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = getResources().getString(R.string.date, year, month + 1, dayOfMonth);
                        mEditPetBirth.setText(date);
                    }
                }, yy, mm, dd);
                mDatePickerDialog.show();
            }
        });
    }

    /**
     * 根据选择的宠物类型,变换可以选择的宠物品种
     * @param checkedId
     */
    private void setSpinnerArray(int checkedId) {
        List<String> entries = new ArrayList<>();
        if (checkedId == R.id.radio_dog) {
            String[] dogArray = getResources().getStringArray(R.array.dogTypeArray);
            entries = Arrays.asList(dogArray);
        } else if (checkedId == R.id.radio_cat) {
            String[] dogArray = getResources().getStringArray(R.array.catTypeArray);
            entries = Arrays.asList(dogArray);
        }
        mPetType = 1; // 重置默认类型为1;
        mPetAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, entries);
        mEditPetType.setAdapter(mPetAdapter);
        mEditPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //type 按position,后面可以优化
                mPetType = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *选取体重单位——wankui
     */
    private void setWeightUnit(){
        mUnit = 1;
        mEditPetWeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mUnit = position+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        setWeightUnit();
        int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radio_dog) {
            mRequestParam.type = 1;
        } else if (checkedRadioButtonId == R.id.radio_cat) {
            mRequestParam.type = 2;
        }

        mRequestParam.photo = mPetAvatar;
        mRequestParam.petName = mEditPetName.getText().toString().trim();
        mRequestParam.breed = mPetType;
        mRequestParam.birth = mEditPetBirth.getText().toString().trim();
        mRequestParam.weight = mEditPetWeight.getText().toString().trim();
        mRequestParam.unit = mUnit;

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
