package com.punuo.pet.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.punuo.pet.member.adapter.MemberHomeAdapter;
import com.punuo.pet.member.module.MemberHeadModule;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.account.UserManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.model.UserInfo;
import com.punuo.sys.sdk.util.DeviceHelper;
import com.punuo.sys.sdk.util.MMKVUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 我的页面
 **/
@Route(path = MemberRouter.ROUTER_MEMBER_FRAGMENT)
public class MemberFragment extends BaseFragment {
    @BindView(R2.id.pull_to_refresh)
    PullToRefreshRecyclerView mPullToRefresh;
    private RecyclerView mRecyclerView;
    private MemberHomeAdapter mMemberHomeAdapter;
    private MemberHeadModule mMemberHeadModule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_member, container, false);
        ButterKnife.bind(this, mFragmentView);
        initView();
        EventBus.getDefault().register(this);
        return mFragmentView;
    }

    private void initView() {
        mPullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mRecyclerView = mPullToRefresh.getRefreshableView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mMemberHomeAdapter = new MemberHomeAdapter(getActivity(), new ArrayList<String>());
        LinearLayout headView = new LinearLayout(getActivity());
        headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headView.setOrientation(LinearLayout.VERTICAL);
        mMemberHomeAdapter.setHeaderView(headView);
        mMemberHeadModule = new MemberHeadModule(getActivity(), headView);
        headView.addView(mMemberHeadModule.getView());

        mRecyclerView.setAdapter(mMemberHomeAdapter);
        mPullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                UserManager.getUserInfo(AccountManager.getUserName());
            }
        });
        checkVersion();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfo userInfo) {
        if (mMemberHeadModule != null) {
            mMemberHeadModule.updateUserInfo(userInfo);
        }
        mPullToRefresh.onRefreshComplete();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            checkVersion();
        }
    }

    private void checkVersion() {
        if (mMemberHeadModule != null) {
            int remoteVersionCode = MMKVUtil.getInt("remote_version_code", DeviceHelper.getVersionCode());
            String remoteVersionName = MMKVUtil.getString("remote_version_name", DeviceHelper.getVersionName());
            if (remoteVersionCode > DeviceHelper.getVersionCode()) {
                mMemberHeadModule.updateVersionDisplay(remoteVersionName, true);
            } else  {
                mMemberHeadModule.updateVersionDisplay(DeviceHelper.getVersionName(), false);
            }
        }
    }
}
