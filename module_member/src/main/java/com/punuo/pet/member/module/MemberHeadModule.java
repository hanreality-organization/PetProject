package com.punuo.pet.member.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.punuo.pet.member.R;
import com.punuo.pet.member.pet.model.InfoData;
import com.punuo.pet.member.request.GetIdModel;
import com.punuo.pet.member.request.GetIdRequest;
import com.punuo.pet.member.request.LogoutRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.pet.update.AutoUpdateService;
import com.punuo.sip.dev.DevManager;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.model.LanguageModel;
import com.punuo.sys.sdk.model.UserInfo;
import com.punuo.sys.sdk.util.DataClearUtil;
import com.punuo.sys.sdk.util.IntentUtil;
import com.punuo.sys.sdk.util.LanguageUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2020-01-07.
 **/
public class MemberHeadModule {
    private View mView;
    private View baseInfoContainer;
    private TextView mBuff;
    private TextView mNickname;
    private ImageView mAvatar;
    private Context mContext;
    private TextView mVersionName;
    private TextView mLanguageText;

    private int languageSelectIndex;

    public View getView() {
        return mView;
    }

    public MemberHeadModule(Context context, ViewGroup parent) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.member_head_layout, parent, false);
        ButterKnife.bind(this, mView);
        initView();
    }

    private void initView() {
        baseInfoContainer = mView.findViewById(R.id.base_information);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (baseInfoContainer.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) baseInfoContainer.getLayoutParams()).topMargin = StatusBarUtil.getStatusBarHeight(mContext);
            }
        } else {
            if (baseInfoContainer.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) baseInfoContainer.getLayoutParams()).topMargin = 0;
            }
        }
        TextView exitButton = mView.findViewById(R.id.exit_button);
        mNickname = mView.findViewById(R.id.user_nickname);
//        mId = mView.findViewById(R.id.user_id);
        mAvatar = mView.findViewById(R.id.user_avatar);
        View account = mView.findViewById(R.id.account);
        View cache = mView.findViewById(R.id.cache);
        View about = mView.findViewById(R.id.about);
        View customer = mView.findViewById(R.id.customerservice);
        View editInfo = mView.findViewById(R.id.edit_info);
        View mShop = mView.findViewById(R.id.shop);
        View update = mView.findViewById(R.id.update_service);
        View wifiConnected = mView.findViewById(R.id.wificonnected);
        View petManager = mView.findViewById(R.id.pet_manager);
        View resetDev = mView.findViewById(R.id.reset_dev);
        View language = mView.findViewById(R.id.language);
        mLanguageText = mView.findViewById(R.id.language_text);
        mVersionName = mView.findViewById(R.id.current_version);
        mBuff = mView.findViewById(R.id.buff);
        mBuff.setText(DataClearUtil.getTotalCacheSize(mContext));

        LanguageModel currentLanguageModel = LanguageUtil.INSTANCE.getCurrentLanguageModel();
        if (currentLanguageModel != null) {
            mLanguageText.setText(currentLanguageModel.getLanguageName());
            languageSelectIndex = InfoData.INSTANCE.getLanguageList().indexOf(currentLanguageModel);
        } else {
            String systemLanguageName = LanguageUtil.INSTANCE.getSystemLanguageName();
            mLanguageText.setText(systemLanguageName);
        }

        //获取id
        getShopId();
        //设置用户头像
        Glide.with(mContext).load(AccountManager.getUserInfo().avatar).into(mAvatar);
        //设置用户昵称
        mNickname.setText(AccountManager.getUserInfo().nickName);
//        mId.setText(AccountManager.getUserName());
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(AccountManager.getSession());
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_DEVICE_MANAGER_ACTIVITY).navigation();
            }
        });
        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClearUtil.cleanAllCache(mContext);
                mBuff.setText(DataClearUtil.getTotalCacheSize(mContext));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ABOUT_ACTIVITY).navigation();
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_CUETOMERSERVICE).navigation();
            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_USER_INFO_ACTIVITY)
                        .withBoolean("canEdit", true)
                        .navigation();
            }
        });
        mShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(SDKRouter.ROUTER_WEB_VIEW_ACTIVITY)
                        .withString("title", PnApplication.getInstance().getString(R.string.ms_shop))
                        .withString("url", "http://pet.qinqingonline.com:8001/#/?userId=" + Constant.SHOPID).navigation();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AutoUpdateService.class);
                intent.putExtra("needToast", true);
                IntentUtil.startServiceInSafeMode(mContext, intent);
            }
        });
        wifiConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(HomeRouter.ROUTER_HOTSPOT_CONNECT_WIFI).navigation();
            }
        });
        petManager.setOnClickListener(v -> {
            ARouter.getInstance().build(HomeRouter.ROUTER_PET_MANAGER_ACTIVITY).navigation();
        });

        resetDev.setOnClickListener(v -> {
            if (DevManager.getInstance().isBindDevice()) {
                DevManager.getInstance().clearDevConfirm(mContext, 2);
            } else {
                ToastUtils.showToast(mContext.getString(R.string.no_device));
            }
        });

        language.setOnClickListener(v -> {
            OptionsPickerView<LanguageModel> pickerView
                    = new OptionsPickerBuilder(mContext, (options1, options2, options3, v1) -> {
                languageSelectIndex = options1;
                mLanguageText.setText(InfoData.INSTANCE.getLanguageList().get(options1).getLanguageName());
                LanguageUtil.INSTANCE.changeLanguage(mContext, InfoData.INSTANCE.getLanguageList().get(options1));
                ARouter.getInstance()
                        .build(SDKRouter.ROUTER_LANGUAGE_ENTRY_ACTIVITY)
                        .navigation();
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }).build();
            pickerView.setSelectOptions(languageSelectIndex);
            pickerView.setTitleText(mContext.getString(R.string.string_language_choose));
            pickerView.setPicker(InfoData.INSTANCE.getLanguageList());
            pickerView.show();
        });
    }

    private LogoutRequest mLogoutRequest;

    private void logout(String userName) {
        if (mLogoutRequest != null && !mLogoutRequest.isFinish()) {
            return;
        }
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingDialog(mContext.getString(R.string.exiting));
        }
        mLogoutRequest = new LogoutRequest();
        mLogoutRequest.addUrlParam("userName", userName);
        mLogoutRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).dismissLoadingDialog();
                }
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    AccountManager.clearAccountData();
                    ARouter.getInstance().build(MemberRouter.ROUTER_LOGIN_ACTIVITY).navigation();
                    if (mContext instanceof BaseActivity) {
                        ((BaseActivity) mContext).finish();
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mLogoutRequest);
    }

    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            //设置用户头像
            Glide.with(mContext).load(userInfo.avatar).into(mAvatar);
            //设置用户昵称
            mNickname.setText(userInfo.nickName);
        }

    }

    private GetIdRequest mGetIdRequest;

    private void getShopId() {
        if (mGetIdRequest != null && !mGetIdRequest.isFinish()) {
            return;
        }
        mGetIdRequest = new GetIdRequest();
        mGetIdRequest.addUrlParam("username", AccountManager.getUserName());
        mGetIdRequest.setRequestListener(new RequestListener<GetIdModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(GetIdModel result) {
                Constant.SHOPID = result.shopId;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetIdRequest);
    }

    public void updateVersionDisplay(String versionName, boolean isNew) {
        if (isNew) {
            mVersionName.setTextColor(Color.parseColor("#ff1940"));
            mVersionName.setText(mContext.getString(R.string.new_version, versionName));
        } else {
            mVersionName.setTextColor(Color.parseColor("#666666"));
            mVersionName.setText(mContext.getString(R.string.current_version, versionName));
        }

    }
}
