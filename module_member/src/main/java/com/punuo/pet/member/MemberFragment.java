package com.punuo.pet.member;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.punuo.pet.member.request.LogoutRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.DataClearUtil;
import com.punuo.sys.sdk.util.IntentUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 我的页面
 **/
@Route(path = MemberRouter.ROUTER_MEMBER_FRAGMENT)
public class MemberFragment extends BaseFragment implements View.OnClickListener {

//    @BindView(R2.id.shop)
//    RelativeLayout mShop;
    Unbinder unbinder;
    private ImageView mBack;
    private TextView mExitButton;
    private TextView mNickname;
    private ImageView mAvater;
    private TextView mBuff;
    private RelativeLayout mAccount;
    private RelativeLayout mCache;
    private RelativeLayout mSystem;
    private RelativeLayout mAbout;
    private RelativeLayout mCustomer;
    private RelativeLayout mEditInfo;
    private Button mcheck;

    private RelativeLayout mshop;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_member, container, false);

        initView();

        View statusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            statusBar.setVisibility(View.VISIBLE);
            statusBar.requestLayout();
        }

        /**
         * 计划准备用于修改个人信息后的刷新，但是没起到作用。暂且保留
         */
        swipeRefreshLayout = mFragmentView.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //设置用户头像
                                String avater = AccountManager.getUserInfo().avatar;
                                Glide.with(getActivity()).load(avater).into(mAvater);

                                //设置用户昵称
                                String nickname = AccountManager.getUserInfo().nickName;
                                mNickname.setText(nickname);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
        unbinder = ButterKnife.bind(this, mFragmentView);
        return mFragmentView;
    }

    private void initView() {
        mcheck = mFragmentView.findViewById(R.id.check);
        mExitButton = mFragmentView.findViewById(R.id.exit_button);
        mNickname = mFragmentView.findViewById(R.id.user_nickname);
        mAvater = mFragmentView.findViewById(R.id.user_avater);
        mAccount = mFragmentView.findViewById(R.id.account);
        mBuff = mFragmentView.findViewById(R.id.buff);
        mCache = mFragmentView.findViewById(R.id.cache);
        mSystem = mFragmentView.findViewById(R.id.system_news);
        mAbout = mFragmentView.findViewById(R.id.about);
        mCustomer = mFragmentView.findViewById(R.id.customerservice);
        mEditInfo = mFragmentView.findViewById(R.id.edit_info);
        mshop = mFragmentView.findViewById(R.id.shop);

        mBuff.setText(DataClearUtil.getTotalCacheSize(getActivity()));

        mcheck.setOnClickListener(this);
        mExitButton.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mCache.setOnClickListener(this);
        mSystem.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mCustomer.setOnClickListener(this);
        mEditInfo.setOnClickListener(this);
        mshop.setOnClickListener(this);

        //设置用户头像
        String avater = AccountManager.getUserInfo().avatar;
        Glide.with(getActivity()).load(avater).into(mAvater);

        //设置用户昵称
        String nickname = AccountManager.getUserInfo().nickName;
        mNickname.setText(nickname);

        //设置用户ID

        //积分商城
        String url = "";
        IntentUtil.openWebViewActivity(getActivity(),url);

//        mShop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    ARouter.getInstance().build(MemberRouter.ROUTER_TEST_ACTIVITY).navigation();
//            }
//        });
    }


    private LogoutRequest mLogoutRequest;

    public void logout(String userName) {
        if (mLogoutRequest != null && !mLogoutRequest.isFinish()) {
            return;
        }
        showLoadingDialog("正在退出...");
        mLogoutRequest = new LogoutRequest();
        mLogoutRequest.addUrlParam("userName", userName);
        mLogoutRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    AccountManager.clearAccountData();
                    ARouter.getInstance().build(MemberRouter.ROUTER_LOGIN_ACTIVITY).navigation();
                    getActivity().finish();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mLogoutRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.exit_button) {//退出登录
            logout(AccountManager.getSession());
        } else if (id == R.id.check) {//签到
            mcheck.setText("已签到");
            mcheck.setBackgroundResource(R.drawable.button_check_nor);
        } else if (id == R.id.account) {//账户管理
            ARouter.getInstance().build(MemberRouter.ROUTER_ACCOUNT_MANAGEMENT_ACTIVITY).navigation();
        } else if (id == R.id.system_news) {//系统消息
            ARouter.getInstance().build(MemberRouter.ROUTER_SYSTEM_NEWS_ACTIVITY).navigation();
        } else if (id == R.id.cache) {//清除缓存
            DataClearUtil.cleanAllCache(getActivity());
            mBuff.setText(DataClearUtil.getTotalCacheSize(getActivity()));
        } else if (id == R.id.about) {//关于我们
            ARouter.getInstance().build(MemberRouter.ROUTER_ABOUT_ACTIVITY).navigation();
        } else if (id == R.id.customerservice) {//客服
            ARouter.getInstance().build(MemberRouter.ROUTER_CUETOMERSERVICE).navigation();
        } else if (id == R.id.edit_info) {//编辑个人信息
            ARouter.getInstance().build(MemberRouter.ROUTER_EDITUSERINFO_ACTIVITY).navigation();
        }
        else  if (id==R.id.shop){
            ARouter.getInstance().build(MemberRouter.ROUTER_TEST_ACTIVITY).navigation();
        }
    }

}
