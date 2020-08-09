package com.punuo.sip.real_weight;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;
import com.punuo.sys.sdk.util.ToastUtils;

import org.zoolu.sip.message.Message;
@Route(path= ServicePath.PATH_UPDATE_WEIGHT)
public class UpdateWeightService extends NormalRequestService<String> {

    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, String result) {
        Log.i("update_weight", "通知设备重新获取最新重量...");
    }

    @Override
    protected void onError(Exception e) {

    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
