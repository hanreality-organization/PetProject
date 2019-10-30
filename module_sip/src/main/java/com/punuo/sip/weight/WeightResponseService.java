package com.punuo.sip.weight;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.zoolu.sip.message.Message;

@Route(path = ServicePath.PATH_WEIGHT_RESPONSE)
public  class WeightResponseService extends NormalRequestService<WeightData> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, WeightData result) {
        Log.i(TAG, "成功获得称重信息");
        ToastUtils.showToast("成功获得称重信息");
        String quality = result.quality;
        EventBus.getDefault().post(quality);
    }

    @Override
    protected void onError(Exception e) {

    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
