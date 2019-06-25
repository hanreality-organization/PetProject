package com.punuo.pet.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.punuo.pet.R;
import com.punuo.pet.circle.CircleFragment;
import com.punuo.pet.home.fragment.HomeFragment;
import com.punuo.pet.login.ILoginCallBack;
import com.punuo.pet.login.LoginManager;
import com.punuo.pet.message.MessageFragment;
import com.punuo.pet.mine.MineFragment;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-06-15.
 **/
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final int TAB_ONE = 0;
    private static final int TAB_TWO = 1;
    private static final int TAB_THREE = 2;
    private static final int TAB_FOUR = 3;
    private static final int TAB_FIVE = 4;

    @Bind(R.id.tab_one)
    View mTabOne;
    @Bind(R.id.tab_two)
    View mTabTwo;
    @Bind(R.id.tab_three)
    View mTabThree;
    @Bind(R.id.tab_four)
    View mTabFour;
    @Bind(R.id.tab_five)
    View mTabFive;
    private LoginManager mLoginManager;
    public static final int TAB_COUNT = 5;
    private MyFragmentManager mMyFragmentManager;
    private View[] mTabBars = new View[TAB_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        ButterKnife.bind(this);
        mLoginManager = new LoginManager(this, new ILoginCallBack() {
            @Override
            public void loginSuccess() {

            }

            @Override
            public void loginError() {

            }

            @Override
            public void getAuthCodeSuccess() {

            }

            @Override
            public void getAuthCodeError() {

            }

            @Override
            public void setPasswordSuccess() {

            }

            @Override
            public void logoutSuccess() {

            }
        });
        mMyFragmentManager = new MyFragmentManager(this);
        init();

        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, true);
    }

    private void init() {
        initTabBars();
        switchFragment(TAB_ONE);
    }

    private void initTabBars() {
        mTabBars[0] = mTabOne;
        mTabBars[1] = mTabTwo;
        mTabBars[2] = mTabThree;
        mTabBars[3] = mTabFour;
        mTabBars[4] = mTabFive;
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
        switch (index) {
            case TAB_ONE:
                mMyFragmentManager.switchFragmentWithCache(HomeFragment.class.getName(), bundle);
                break;
            case TAB_TWO:
                mMyFragmentManager.switchFragmentWithCache(CircleFragment.class.getName(), bundle);
                break;
            case TAB_THREE:
                mMyFragmentManager.switchFragmentWithCache(MessageFragment.class.getName(), bundle);
                break;
            case TAB_FOUR:
                mMyFragmentManager.switchFragmentWithCache(MineFragment.class.getName(), bundle);
                break;
            case TAB_FIVE:
//                mMyFragmentManager.switchFragmentWithCache(PersonFragment.class.getName(), bundle);
                break;
            default:
                break;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_one:
                switchFragment(TAB_ONE);
                break;
            case R.id.tab_two:
                switchFragment(TAB_TWO);
                break;
            case R.id.tab_three:
                switchFragment(TAB_THREE);
                break;
            case R.id.tab_four:
                switchFragment(TAB_FOUR);
                break;
            case R.id.tab_five:
                switchFragment(TAB_FIVE);
                break;
        }
    }
}
