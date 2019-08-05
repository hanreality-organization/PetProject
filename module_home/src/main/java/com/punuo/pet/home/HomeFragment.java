package com.punuo.pet.home;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.punuo.pet.PetManager;
import com.punuo.pet.home.view.PetLoopHolder;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.TimeUtils;
import com.punuo.sys.sdk.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 首页
 **/
@Route(path = HomeRouter.ROUTER_HOME_FRAGMENT)
public class HomeFragment extends BaseFragment {
    @BindView(R2.id.care_pet)
    RoundedImageView mCarePet;
    private ViewGroup mHeadContainer;
    private PetLoopHolder mPetLoopHolder;
    private TextView mPetName;
    private View mAddPet;
    private ViewGroup mPetInfoContainer;
    private View mPetInfo;
    private TextView mPetAge;
    private TextView mPetWeight;
    private ImageView mDevicePart;
    private View mFeetPet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        EventBus.getDefault().register(this);
        PetManager.getPetInfo();
        return mFragmentView;
    }

    private void initView() {
        mHeadContainer = mFragmentView.findViewById(R.id.header_container);
        mPetInfoContainer = mFragmentView.findViewById(R.id.home_pet_info_container);
        mAddPet = mFragmentView.findViewById(R.id.home_add_pet);
        mPetName = mFragmentView.findViewById(R.id.home_pet_name);
        mDevicePart = mFragmentView.findViewById(R.id.device_part);
        mFeetPet = mFragmentView.findViewById(R.id.feed_pet);
        mPetLoopHolder = PetLoopHolder.newInstance(getActivity(), mHeadContainer);
        mHeadContainer.addView(mPetLoopHolder.getRootView());
        initPetInfo();

        mAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY)
                        .navigation();
            }
        });
        mDevicePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mFeetPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mCarePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initPetInfo() {
        mPetInfo = LayoutInflater.from(getActivity()).inflate(R.layout.home_pet_info_layout,
                mPetInfoContainer, false);
        mPetInfoContainer.addView(mPetInfo);
        mPetAge = mPetInfo.findViewById(R.id.pet_age);
        mPetWeight = mPetInfo.findViewById(R.id.pet_weight);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final PetModel model) {
        if (model == null) {
            return;
        }
        mPetLoopHolder.updateView(model);
        updatePet(0, model);
        mPetLoopHolder.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updatePet(position, model);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updatePet(int position, PetModel model) {
        if (model.mPets.isEmpty()) {
            mPetInfoContainer.setVisibility(View.GONE);
            return;
        }
        mPetInfoContainer.setVisibility(View.VISIBLE);
        PetData petData = model.mPets.get(position);
        ViewUtil.setText(mPetName, petData.petname);
        String age = TimeUtils.formatAge(TimeUtils.calAgeMonth(petData.birth));
        ViewUtil.setText(mPetAge, age);
        ViewUtil.setText(mPetWeight, String.valueOf(petData.weight));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
