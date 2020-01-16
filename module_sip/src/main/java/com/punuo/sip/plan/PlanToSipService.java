package com.punuo.sip.plan;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;
import com.punuo.sys.sdk.util.ToastUtils;


import org.zoolu.sip.message.Message;

@Route(path= ServicePath.PLAN_TOSIP)
public class PlanToSipService extends NormalRequestService<String> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, String result) {
        Log.i("plan", "喂食计划成功发送给服务器 ");
        if(result != null){
            ToastUtils.showToast("喂食计划保存/更新成功");
        }
    }

    @Override
    protected void onError(Exception e) {

    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
