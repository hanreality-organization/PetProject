package com.punuo.pet.compat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.PetManager;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.CircleRouter;
import com.punuo.pet.router.CompatRouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.MessageRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.model.UserInfo;
import com.punuo.sys.sdk.util.RegexUtils;
import com.punuo.sys.sdk.util.StatusBarUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        mMyFragmentManager = new MyFragmentManager(this);
        init();

        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, true);//StatusBarUtil：状态栏工具类
        mPostView = getWindow().getDecorView();
        mPostView.post(new Runnable() {//view.post():1.子线程更UI,2.获取View的宽高
            @Override
            public void run() {
                if (AccountManager.isLoginned()) {
                    String session = AccountManager.getSession();
                    if (!RegexUtils.checkMobile(session)) {
                        Bundle params = new Bundle();
                        params.putString("openId", session);
                        ARouter.getInstance().build(MemberRouter.ROUTER_BIND_PHONE_ACTIVITY)
                                .with(params)
                                .navigation();
                    } else {
                        PetManager.getPetInfo();
                    }
                }
            }
        });
    }

    private void init() {
        initTabBars();
        switchFragment(TAB_ONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfo userInfo) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel model) {
        if (model.mPets == null || model.mPets.isEmpty()) {
            ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY)
                    .navigation();
        }
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

    @Override
    public void onBackPressed() {

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
                fragment = (Fragment) ARouter.getInstance().build(CircleRouter.ROUTER_CIRCLE_FRAGMENT).navigation();
                break;
            case TAB_THREE:
                fragment = (Fragment) ARouter.getInstance().build(MessageRouter.ROUTER_MESSAGE_FRAGMENT).navigation();
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
}
