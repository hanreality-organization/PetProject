package com.punuo.sip.real_weight;

import android.app.Service;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.model.LatestWeightData;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;

import org.greenrobot.eventbus.EventBus;
import org.zoolu.sip.message.Message;
@Route(path= ServicePath.PATH_RECEIVE_WEIGHT)
public class ReceiveLatestWeightService extends NormalRequestService<LatestWeightData> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, LatestWeightData result) {
        EventBus.getDefault().post(result);
    }

    @Override
    protected void onError(Exception e) {

    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
