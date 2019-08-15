package com.punuo.pet.home.module;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.view.PetLoopHolder;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.util.TimeUtils;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class HomeHeadModule {

    @BindView(R2.id.header_container)
    LinearLayout mHeaderContainer;
    @BindView(R2.id.home_pet_info_container)
    LinearLayout mHomePetInfoContainer;
    @BindView(R2.id.home_add_pet)
    ImageView mHomeAddPet;
    @BindView(R2.id.home_pet_name)
    TextView mHomePetName;
    @BindView(R2.id.device_part)
    RoundedImageView mDevicePart;
    @BindView(R2.id.device_container)
    LinearLayout mDeviceContainer;
    @BindView(R2.id.feed_pet)
    RoundedImageView mFeedPet;
    @BindView(R2.id.care_pet)
    RoundedImageView mCarePet;

    private View mRootView;
    private PetLoopHolder mPetLoopHolder;
    private Context mContext;
    private TextView mPetAge;
    private TextView mPetWeight;
    private PetData mCurrentPetData;

    public HomeHeadModule(Context context, ViewGroup parent) {
        mContext = context;
        mRootView = LayoutInflater.from(context)
                .inflate(R.layout.home_head_module_layout, parent, false);
        ButterKnife.bind(this, mRootView);
        mPetLoopHolder = PetLoopHolder.newInstance(context, mHeaderContainer);
        mHeaderContainer.addView(mPetLoopHolder.getRootView());
        initPetInfo();
    }

    public View getRootView() {
        return mRootView;
    }

    private void initPetInfo() {
        View mPetInfo = LayoutInflater.from(mContext).inflate(R.layout.home_pet_info_layout,
                mHomePetInfoContainer, false);
        mHomePetInfoContainer.addView(mPetInfo);
        mPetAge = mPetInfo.findViewById(R.id.pet_age);
        mPetWeight = mPetInfo.findViewById(R.id.pet_weight);
    }

    public void updateView(final PetModel petModel) {
        mPetLoopHolder.updateView(petModel);
        updatePet(0, petModel);
        mPetLoopHolder.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updatePet(position, petModel);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mHomeAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY)
                        .navigation();
            }
        });
        mDevicePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_CONNECT_DEVICE_ACTIVITY)
                        .navigation();
            }
        });
        mFeedPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPetData == null) {
                    ToastUtils.showToast("暂无宠物信息，请添加宠物");
                } else {
                    ARouter.getInstance().build(HomeRouter.ROUTER_FEED_ACTIVITY)
                            .withParcelable("petData", mCurrentPetData)
                            .navigation();
                }
            }
        });
        mCarePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_CARE_ACTIVITY)
                        .withParcelable("petData", mCurrentPetData)
                        .navigation();
            }
        });
    }

    private void updatePet(int position, PetModel model) {
        if (model.mPets.isEmpty()) {
            mHomePetInfoContainer.setVisibility(View.GONE);
            return;
        }
        mHomePetInfoContainer.setVisibility(View.VISIBLE);
        mCurrentPetData = model.mPets.get(position);
        ViewUtil.setText(mHomePetName, mCurrentPetData.petname);
        String age = TimeUtils.formatAge(TimeUtils.calAgeMonth(mCurrentPetData.birth));
        ViewUtil.setText(mPetAge, age);
        ViewUtil.setText(mPetWeight, String.valueOf(mCurrentPetData.weight));
    }
}
