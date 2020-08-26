package com.punuo.pet.compat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.PetManager;
import com.punuo.pet.compat.process.HeartBeatTaskResumeProcessor;
import com.punuo.pet.event.SelectDeviceEvent;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.CompatRouter;
import com.punuo.pet.router.DeviceType;
import com.punuo.pet.router.FeedRouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.pet.update.AutoUpdateService;
import com.punuo.sip.HeartBeatHelper;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.event.LoginFailEvent;
import com.punuo.sip.event.ReRegisterEvent;
import com.punuo.sip.model.LoginResponse;
import com.punuo.sip.request.SipGetUserIdRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.account.UserManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.model.UserInfo;
import com.punuo.sys.sdk.util.IntentUtil;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.util.RegexUtils;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/

@Route(path = CompatRouter.ROUTER_HOME_ACTIVITY)
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final int TAB_ONE = 0;
    private static final int TAB_TWO = 1;
    private static final int TAB_THREE = 2;
    private static final int TAB_FOUR = 3;
    private static final int TAB_FIVE = 4;

    public static final int TAB_COUNT = 5;
    private MyFragmentManager mMyFragmentManager;
    private View[] mTabBars = new View[TAB_COUNT];
    private View mPostView;
    private boolean loginFailed = false;

    public static final int MSG_HEART_BEAR_VALUE = 1;
    private HeartBeatTaskResumeProcessor mHeartBeatTaskResumeProcessor;
    private long mExtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        mMyFragmentManager = new MyFragmentManager(this);
        init();
        mHeartBeatTaskResumeProcessor = new HeartBeatTaskResumeProcessor(mBaseHandler);
        mHeartBeatTaskResumeProcessor.onCreate();
        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, true); //StatusBarUtil：状态栏工具类
        mPostView = getWindow().getDecorView();
        mPostView.post(new Runnable() {//view.post():1.子线程更UI,2.获取View的宽高
            @Override
            public void run() {
                if (AccountManager.isLoginned()) {
                    String session = AccountManager.getSession();
                    if (!RegexUtils.checkMobile(session)) {
                        //未绑定手机号(适用于微信登陆)
                        Bundle params = new Bundle();
                        params.putString("openId", session);
                        ARouter.getInstance().build(MemberRouter.ROUTER_BIND_PHONE_ACTIVITY)
                                .with(params)
                                .navigation();
                    } else {
                        //获取宠物信息
                        PetManager.getPetInfo();
                    }
                }
            }
        });
        EventBus.getDefault().register(this);
        //拉取用户信息
        if (RegexUtils.checkMobile(AccountManager.getUserName())) {
            UserManager.getUserInfo(AccountManager.getUserName());
        }
        //根据本地记录展示底部导航栏的内容
        onDeviceSelect(MMKVUtil.getInt("deviceType", DeviceType.UNKNOWN));
        Intent intent = new Intent(this, AutoUpdateService.class);
        intent.putExtra("needToast", false);
        IntentUtil.startServiceInSafeMode(this, intent);
    }

    private void init() {
        initTabBars();
        switchFragment(TAB_ONE);
    }

    private void initTabBars() {
        mTabBars[0] = findViewById(R.id.tab_one);
        mTabBars[1] = findViewById(R.id.tab_two);
        mTabBars[2] = findViewById(R.id.tab_three);
        mTabBars[3] = findViewById(R.id.tab_four);
        mTabBars[4] = findViewById(R.id.tab_five);
        for (int i = 0; i < TAB_COUNT; i++) {
            mTabBars[i].setOnClickListener(this);
        }
    }

    /**
     * Sip服务注册
     */
    private void getSipUserID() {
        SipGetUserIdRequest getUserIdRequest = new SipGetUserIdRequest();
        SipUserManager.getInstance().addRequest(getUserIdRequest);
    }

    /**
     * 用户信息返回
     * @param userInfo userInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfo userInfo) {
        getSipUserID();
    }

    /**
     * 宠物信息返回，为空：跳转到添加宠物页面
     * @param model
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel model) {
        if (model.mPets == null || model.mPets.isEmpty()) {
            ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY)
                    .navigation();
        }
    }

    /**
     * Sip服务重新注册事件
     * @param event event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReRegisterEvent event) {
        if (loginFailed) {
            loginFailed = false;
            return;
        }
        mBaseHandler.removeMessages(MSG_HEART_BEAR_VALUE);
        getSipUserID();
    }

    /**
     * Sip服务注册失败事件
     * @param event event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginFailEvent event) {
        mBaseHandler.removeMessages(MSG_HEART_BEAR_VALUE);
        loginFailed = true;
    }

    /**
     * Sip服务注册成功事件
     * @param event event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginResponse event) {
        //sip登陆注册成功 开启心跳保活
        if (!mBaseHandler.hasMessages(MSG_HEART_BEAR_VALUE)) {
            mBaseHandler.sendEmptyMessageDelayed(MSG_HEART_BEAR_VALUE, HeartBeatHelper.DELAY);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectDeviceEvent event) {
        onDeviceSelect(event.deviceType);
    }

    /**
     * 空实现返回事件
     */
    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExtTime) > 2000) {
                mExtTime = System.currentTimeMillis();
            } else {
                ToastUtils.closeToast();
                if (!isFinishing()) {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void switchFragment(int index) {
        changeTab(index);
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        switch (index) {
            case TAB_ONE:
                fragment = (Fragment) ARouter.getInstance().build(HomeRouter.ROUTER_HOME_FRAGMENT).navigation();
                break;
            case TAB_TWO:
                fragment = (Fragment) ARouter.getInstance().build(FeedRouter.ROUTER_FEED_HOME_FRAGMENT).navigation();
                break;
            case TAB_THREE:
                fragment = (Fragment) ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_FRAGMENT).navigation();
                break;
            case TAB_FOUR:
                fragment = (Fragment) ARouter.getInstance().build(MemberRouter.ROUTER_MEMBER_FRAGMENT).navigation();
                break;
            case TAB_FIVE:
//                mMyFragmentManager.switchFragmentWithCache(PersonFragment.class.getName(), bundle);
                break;
            default:
                break;
        }
        if (fragment != null) {
            mMyFragmentManager.switchFragmentWithCache(fragment.getClass().getName(), bundle);
        }
    }

    private void changeTab(int type) {
        for (int i = 0; i < TAB_COUNT; ++i) {
            if (i == type) {
                mTabBars[i].setSelected(true);
            } else {
                mTabBars[i].setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        int type = -1;
        for (int i = 0; i < mTabBars.length; i++) {
            if (mTabBars[i].getId() == id) {
                type = i;
            }
        }
        if (type == -1) {
            return;
        }
        switchFragment(type);
    }

    /**
     * 设备选择之后刷新底部导航栏理的内容
     */
    public void onDeviceSelect(int deviceType) {
        switch (deviceType) {
            case DeviceType.FEED:
                mTabBars[1].setVisibility(View.VISIBLE);
                mTabBars[2].setVisibility(View.VISIBLE);
                break;
            case DeviceType.MAOCE:
                mTabBars[1].setVisibility(View.GONE);
                mTabBars[2].setVisibility(View.GONE);
                break;
            case DeviceType.UNKNOWN:
            default:
                mTabBars[1].setVisibility(View.GONE);
                mTabBars[2].setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_HEART_BEAR_VALUE:
                if (AccountManager.isLoginned()) {
                    HeartBeatHelper.heartBeat();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mHeartBeatTaskResumeProcessor != null) {
            mHeartBeatTaskResumeProcessor.onDestroy();
        }
    }
}
