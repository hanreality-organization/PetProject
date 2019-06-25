package com.punuo.pet.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.punuo.pet.Constant;
import com.punuo.pet.event.AuthEvent;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2019-06-22.
 **/
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    protected IWXAPI mIWXAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, false);
        mIWXAPI.registerApp(Constant.WX_APP_ID);
        mIWXAPI.handleIntent(getIntent(), this);
    }



    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("han.chen", "onResp: " + baseResp);
        if (baseResp instanceof SendAuth.Resp) {
            Bundle bundle = new Bundle();
            baseResp.toBundle(bundle);
            EventBus.getDefault().post(new AuthEvent(AuthEvent.TYPE_WEIXIN, bundle));
        }
        finish();
    }
}
