package com.punuo.pet.home.module;

import android.content.Context;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.chart.ColumnView;
import com.punuo.pet.home.view.PetLoopHolder;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.FeedRouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.util.TimeUtils;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.zip.Inflater;

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
    //    @BindView(R2.id.device_part)
//    RoundedImageView mDevicePart;
    @BindView(R2.id.device_container)
    ViewGroup mDeviceContainer;
    @BindView(R2.id.feed_pet)
    RoundedImageView mFeedPet;
    @BindView(R2.id.care_pet)
    RoundedImageView mCarePet;
    @BindView(R2.id.chart_container)
    LinearLayout mChartContainer;
    @BindView(R2.id.home_ads_container)
    LinearLayout mHomeAdsContainer;


    private View mRootView;
    private PetLoopHolder mPetLoopHolder;
    private Context mContext;
    private TextView mPetAge;
    private TextView mPetWeight;
    private PetData mCurrentPetData;


    protected Spinner spinner;

    public HomeHeadModule(Context context, ViewGroup parent) {
        mContext = context;
        mRootView = LayoutInflater.from(context)
                .inflate(R.layout.home_head_module_layout, parent, false);
        ButterKnife.bind(this, mRootView);
        mPetLoopHolder = PetLoopHolder.newInstance(context, mHeaderContainer);
        mHeaderContainer.addView(mPetLoopHolder.getRootView());
        initPetInfo();
        spinner = mRootView.findViewById(R.id.space1);
        barChart1();
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = spinner.getSelectedItem().toString();
                if ("吃粮频率".equals(str)) {
                    mChartContainer.removeAllViews();
                    barChart1();
                }
                if ("吃粮克数".equals(str)) {
                    mChartContainer.removeAllViews();
                    barChart2();
                }
                if ("剩余克数".equals(str)) {
                    mChartContainer.removeAllViews();
                    barChart3();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mHomeAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY)
                        .navigation();
            }
        });
//        mDeviceContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance().build(HomeRouter.ROUTER_BIND_DEVICE_ACTIVITY)
//                        .navigation();
//            }
//        });
        mFeedPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPetData == null) {
                    ToastUtils.showToast("暂无宠物信息，请添加宠物");
                } else {
                    ARouter.getInstance().build(FeedRouter.ROUTER_FEED_HEALTH_ACTIVITY)
                            .withParcelable("petData", mCurrentPetData)
                            .navigation();
                }
            }
        });
        mCarePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPetData == null) {
                    ToastUtils.showToast("暂无宠物信息，请添加宠物");
                } else {
                    ARouter.getInstance().build(HomeRouter.ROUTER_CARE_ACTIVITY)
                            .withParcelable("petData", mCurrentPetData)
                            .navigation();
                }
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

    private void barChart1() {
        //第一个为空，它需要占一个位置
        String[] transverse = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        String[] vertical = {"0", "2h", "4h", "8h", "10h"};
        //这里的数据是根据你横列有几个来设的，如上面的横列星期有周一到周日，所以这里设置七个数据
        int[] data = {420, 380, 340, 300, 260, 220, 180};
        //这里的颜色就对应线条、文字和柱状图（可以根据自己的需要到color里设置）
        List<Integer> color = new ArrayList<>();
        color.add(R.color.colorAccent);
        color.add(R.color.colorPrimary);
        color.add(R.color.blue);
        mChartContainer.addView(new ColumnView(mContext, transverse, vertical, color, data));
    }

    private void barChart2() {
        //第一个为空，它需要占一个位置
        String[] transverse = {"", "星期一", "星期二", "周三", "周四", "周五", "周六", "周日"};
        String[] vertical = {"0", "2h", "4h", "8h", "10h"};
        //这里的数据是根据你横列有几个来设的，如上面的横列星期有周一到周日，所以这里设置七个数据
        int[] data = {420, 380, 340, 300, 260, 220, 180};
        //这里的颜色就对应线条、文字和柱状图（可以根据自己的需要到color里设置）
        List<Integer> color = new ArrayList<>();
        color.add(R.color.colorAccent);
        color.add(R.color.colorPrimary);
        color.add(R.color.blue);
        mChartContainer.addView(new ColumnView(mContext, transverse, vertical, color, data));
    }

    private void barChart3() {
        //第一个为空，它需要占一个位置
        String[] transverse = {"", "本周一", "本周二", "周三", "周四", "周五", "周六", "周日"};
        String[] vertical = {"0", "2h", "4h", "8h", "10h"};
        //这里的数据是根据你横列有几个来设的，如上面的横列星期有周一到周日，所以这里设置七个数据
        int[] data = {420, 380, 340, 300, 260, 220, 180};
        //这里的颜色就对应线条、文字和柱状图（可以根据自己的需要到color里设置）
        List<Integer> color = new ArrayList<>();
        color.add(R.color.colorAccent);
        color.add(R.color.colorPrimary);
        color.add(R.color.blue);
        mChartContainer.addView(new ColumnView(mContext, transverse, vertical, color, data));
    }
}

