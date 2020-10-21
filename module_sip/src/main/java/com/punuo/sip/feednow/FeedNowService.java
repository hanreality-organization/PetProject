package com.punuo.sip.feednow;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.sip.request.BaseSipRequest;
import com.punuo.sip.service.NormalRequestService;
import com.punuo.sip.service.ServicePath;
import com.punuo.sys.sdk.util.ToastUtils;

import org.zoolu.sip.message.Message;

@Route(path= ServicePath.PATH_FEEDNOW_RESPONSE)
public class FeedNowService extends NormalRequestService<FeedNotifyData> {
    @Override
    protected String getBody() {
        return null;
    }

    @Override
    protected void onSuccess(Message msg, FeedNotifyData result) {
        Log.i("feed", "出粮请求成功发送 ");
        if (result != null) {
            String response = "from" + result.userName + ";" + "to" + result.devId + "feed_count" + result.feedCount;
            Log.i("feed", response);
            ToastUtils.showToast("正在出粮，请稍后");
        }
    }

    @Override
    protected void onError(Exception e) {
        ToastUtils.showToast("出粮失败，请重新尝试");
    }

    @Override
    public void handleTimeOut(BaseSipRequest baseSipRequest) {
        ToastUtils.showToast("出粮失败，请重新尝试");
    }
}
