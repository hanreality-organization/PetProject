package com.punuo.pet.member.module;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.punuo.pet.member.R;
import com.punuo.pet.member.request.GetIdModel;
import com.punuo.pet.member.request.GetIdRequest;
import com.punuo.pet.member.request.LogoutRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.pet.update.AutoUpdateService;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.model.UserInfo;
import com.punuo.sys.sdk.util.DataClearUtil;
import com.punuo.sys.sdk.util.DeviceHelper;
import com.punuo.sys.sdk.util.IntentUtil;

import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2020-01-07.
 **/
public class MemberHeadModule {
    private View mView;
    private ImageView mBack;
    private TextView mBuff;
    private TextView mNickname;
    private ImageView mAvatar;
    private Button mCheck;
    private Context mContext;
    private TextView mId;
    private TextView mVersionName;
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
        mCheck = mView.findViewById(R.id.check);
        TextView exitButton = mView.findViewById(R.id.exit_button);
        mNickname = mView.findViewById(R.id.user_nickname);
        mId = mView.findViewById(R.id.user_id);
        mAvatar = mView.findViewById(R.id.user_avatar);
        View account = mView.findViewById(R.id.account);
        View cache = mView.findViewById(R.id.cache);
        View about = mView.findViewById(R.id.about);
        View customer = mView.findViewById(R.id.customerservice);
        View editInfo = mView.findViewById(R.id.edit_info);
        View mShop = mView.findViewById(R.id.shop);
        View update = mView.findViewById(R.id.update_service);
        View wificonnected = mView.findViewById(R.id.wificonnected);
        mVersionName = mView.findViewById(R.id.current_version);
        mBuff = mView.findViewById(R.id.buff);
        mBuff.setText(DataClearUtil.getTotalCacheSize(mContext));

        //获取id
        getShopId();
        //设置用户头像
        Glide.with(mContext).load(AccountManager.getUserInfo().avatar).into(mAvatar);
        //设置用户昵称
        mNickname.setText(AccountManager.getUserInfo().nickName);
        mId.setText(AccountManager.getUserName());
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(AccountManager.getSession());
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ACCOUNT_MANAGEMENT_ACTIVITY).navigation();
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
                ARouter.getInstance().build(MemberRouter.ROUTER_EDITUSERINFO_ACTIVITY).navigation();
            }
        });
        mShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("shopId", ""+Constant.SHOPID);
                ARouter.getInstance().build(SDKRouter.ROUTER_WEB_VIEW_ACTIVITY)
                        .withString("url", "http://feeder.qinqingonline.com:8080/#/?userId="+Constant.SHOPID).navigation();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AutoUpdateService.class);
                IntentUtil.startServiceInSafeMode(mContext, intent);
            }
        });
        wificonnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(HomeRouter.ROUTER_HOTSPOT_CONNECT_WIFI).navigation();
            }
        });
        mVersionName.setText(DeviceHelper.getVersionName());
    }

    private LogoutRequest mLogoutRequest;

    private void logout(String userName) {
        if (mLogoutRequest != null && !mLogoutRequest.isFinish()) {
            return;
        }
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingDialog("正在退出...");
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
    private void getShopId(){
        if(mGetIdRequest!=null&&!mGetIdRequest.isFinish()){
            return;
        }
        mGetIdRequest = new GetIdRequest();
        mGetIdRequest.addUrlParam("username",AccountManager.getUserName());
        mGetIdRequest.setRequestListener(new RequestListener<GetIdModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(GetIdModel result) {
                Log.i("服务器返回的shopId", ""+result.shopId);
                Constant.SHOPID = result.shopId;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetIdRequest);
    }
}
