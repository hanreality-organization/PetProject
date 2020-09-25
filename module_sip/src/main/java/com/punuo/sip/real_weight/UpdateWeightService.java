package com.punuo.sip.real_weight;

import android.util.LayoutDirection;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.model.LatestWeightData;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.zoolu.sip.message.Message;


@Route(path= ServicePath.PATH_UPDATE_WEIGHT)
public class UpdateWeightService extends NormalRequestService<LatestWeightData> {

    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, LatestWeightData result) {
//        Log.i("update_weight", "通知设备重新获取最新重量...");
        if(result!=null){
            Log.i("update_weight", "成功将sip命令发送到服务器");
            ToastUtils.showToast("已成功获取重量！");
        }
        EventBus.getDefault().post(result);
    }

    @Override
    protected void onError(Exception e) {
        ToastUtils.showToast("网络出了一点问题请重试！");
    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
