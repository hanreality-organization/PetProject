package com.punuo.pet.compat.process;

import com.punuo.pet.compat.HomeActivity;
import com.punuo.sip.HeartBeatHelper;
import com.punuo.sys.sdk.app.AbstractTaskResumeProcessor;
import com.punuo.sys.sdk.util.BaseHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 **/
public class HeartBeatTaskResumeProcessor extends AbstractTaskResumeProcessor {

    private BaseHandler mBaseHandler;

    public HeartBeatTaskResumeProcessor(BaseHandler baseHandler) {
        mBaseHandler = baseHandler;
    }

    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void restartTask() {
        mBaseHandler.sendEmptyMessage(HomeActivity.MSG_HEART_BEAR_VALUE);
    }

    @Override
    protected void stopTask() {
        mBaseHandler.removeMessages(HomeActivity.MSG_HEART_BEAR_VALUE);
    }

    @Override
    protected boolean isTaskStopped() {
        return !mBaseHandler.hasMessages(HomeActivity.MSG_HEART_BEAR_VALUE);
    }

    @Override
    protected long getInternalTime() {
        return HeartBeatHelper.DELAY;
    }

    @Override
    protected String getTaskName() {
        return "SIP_HEART_BEAT_TASK";
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
