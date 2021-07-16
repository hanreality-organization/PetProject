package com.punuo.sip.plan;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.R;
import com.punuo.sip.event.AddPlanSuccessEvent;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.zoolu.sip.message.Message;

@Route(path = ServicePath.PLAN_TO_SIP)
public class PlanToSipService extends NormalRequestService<String> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, String result) {
        if (result != null) {
            ToastUtils.showToast(PnApplication.getInstance().getResources().getString(R.string.tip_update_success));
            EventBus.getDefault().post(new AddPlanSuccessEvent());
        }
    }

    @Override
    protected void onError(Exception e) {

    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {

    }
}
