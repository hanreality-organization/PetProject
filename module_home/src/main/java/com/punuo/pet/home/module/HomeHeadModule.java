package com.punuo.pet.home.module;

import android.content.Context;
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
import com.punuo.pet.home.chart.CustomBarChart;
import com.punuo.pet.home.chart.CustomBarChart1;
import com.punuo.pet.home.device.model.ChartData;
import com.punuo.pet.home.device.model.ChartData2;
import com.punuo.pet.home.device.model.ChartData3;
import com.punuo.pet.home.device.request.GetFoodfrequencyRequest;
import com.punuo.pet.home.device.request.GetFoodnumberRequest;
import com.punuo.pet.home.device.request.GetSurplusfoodRequest;
import com.punuo.pet.home.view.GlideImageLoader;
import com.punuo.pet.home.view.PetLoopHolder;
import com.punuo.pet.home.view.model.RotationChartData;
import com.punuo.pet.home.view.request.GetRotationChart;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.FeedRouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.TimeUtils;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.util.ViewUtil;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    @BindView(R2.id.device_container)
    ViewGroup mDeviceContainer;
    @BindView(R2.id.feed_pet)
    RoundedImageView mFeedPet;
    @BindView(R2.id.care_pet)
    RoundedImageView mCarePet;
    @BindView(R2.id.chart_container)
    LinearLayout mChartContainer;
    @BindView(R2.id.bander)
    Banner banner;

    private View mRootView;
    private PetLoopHolder mPetLoopHolder;
    private Context mContext;
    private TextView mPetAge;
    private TextView mPetWeight;
    private PetData mCurrentPetData;

    protected Spinner spinner;
    private String[] ChartX=new String[4]; //柱状图水平坐标
    private int[] Chartdata=new int[4];//柱状图值
    List<String> images=new ArrayList();

    public HomeHeadModule(Context context, ViewGroup parent) {
        mContext = context;
        mRootView = LayoutInflater.from(context)
                .inflate(R.layout.home_head_module_layout, parent, false);
        ButterKnife.bind(this, mRootView);
        mPetLoopHolder = PetLoopHolder.newInstance(context, mHeaderContainer);
        mHeaderContainer.addView(mPetLoopHolder.getRootView());
        initPetInfo();
        spinner = mRootView.findViewById(R.id.space1);
        getrotationchart();
        getFoodfrequency();
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
                if("吃粮频率".equals(str)){ mChartContainer.removeAllViews();getFoodfrequency(); }
                if("吃粮克数".equals(str)){ mChartContainer.removeAllViews();getFoodnumber(); }
                if("剩余克数".equals(str)){ mChartContainer.removeAllViews();getSurplusfood(); }
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
        Constant.petData = mCurrentPetData;
        ViewUtil.setText(mHomePetName, mCurrentPetData.petname);
        String age = TimeUtils.formatAge(TimeUtils.calAgeMonth(mCurrentPetData.birth));
        ViewUtil.setText(mPetAge, age);
        ViewUtil.setText(mPetWeight, String.valueOf(mCurrentPetData.weight));
    }

    private GetFoodfrequencyRequest mGetFoodfrequencyRequest;

    public void getFoodfrequency(){
        if (mGetFoodfrequencyRequest != null && !mGetFoodfrequencyRequest.isFinish()) {
            return;
        }
        mGetFoodfrequencyRequest = new GetFoodfrequencyRequest();
        mGetFoodfrequencyRequest.addUrlParam("userName",AccountManager.getUserName());
        mGetFoodfrequencyRequest.setRequestListener(new RequestListener<ChartData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(ChartData result) {
                if (result == null) {
                    return;
                }
                if(result!=null){
                    ChartX[0]=result.frequencyData.time1;
                    Chartdata[0]=result.frequencyData.frequency1;
                    ChartX[1]=result.frequencyData.time2;
                    Chartdata[1]=result.frequencyData.frequency2;
                    ChartX[2]=result.frequencyData.time3;
                    Chartdata[2]=result.frequencyData.frequency3;
                    ChartX[3]=result.frequencyData.time4;
                    Chartdata[3]=result.frequencyData.frequency4;
                    barChart1();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetFoodfrequencyRequest);
    }

    private GetFoodnumberRequest mGetFoodnumberRequest;
    public void getFoodnumber(){
        if (mGetFoodnumberRequest != null && !mGetFoodnumberRequest.isFinish()) {
            return;
        }
        mGetFoodnumberRequest = new GetFoodnumberRequest();
        mGetFoodnumberRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetFoodnumberRequest.setRequestListener(new RequestListener<ChartData2>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(ChartData2 result) {
                if (result == null) {
                    return;
                }
                if(result!=null){
                    ChartX[0]=result.eatdata.time1;
                    Chartdata[0]=result.eatdata.eat1;
                    ChartX[1]=result.eatdata.time2;
                    Chartdata[1]=result.eatdata.eat2;
                    ChartX[2]=result.eatdata.time3;
                    Chartdata[2]=result.eatdata.eat3;
                    ChartX[3]=result.eatdata.time4;
                    Chartdata[3]=result.eatdata.eat4;
                    barChart2();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetFoodnumberRequest);
    }

    private GetSurplusfoodRequest mGetSurplusfoodRequest;
    public void getSurplusfood(){
        if (mGetSurplusfoodRequest != null && !mGetSurplusfoodRequest.isFinish()) {
            return;
        }
        mGetSurplusfoodRequest = new GetSurplusfoodRequest();
        mGetSurplusfoodRequest.addUrlParam("userName",AccountManager.getUserName());
        mGetSurplusfoodRequest.setRequestListener(new RequestListener<ChartData3>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(ChartData3 result) {
                if (result == null) {
                    return;
                }
                if(result!=null){
                    ChartX[0]=result.leftData.time1;
                    int i1=Integer.parseInt(result.leftData.lefted1);
                    Chartdata[0]=i1;
                    ChartX[1]=result.leftData.time2;
                    int i2=Integer.parseInt(result.leftData.lefted2);
                    Chartdata[1]=i2;
                    ChartX[2]=result.leftData.time3;
                    int i3=Integer.parseInt(result.leftData.lefted3);
                    Chartdata[2]=i3;
                    ChartX[3]=result.leftData.time4;
                    int i4=Integer.parseInt(result.leftData.lefted4);
                    Chartdata[3]=i4;
                    barChart3();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetSurplusfoodRequest);
    }

    private GetRotationChart mGetRotationChart;
    public void getrotationchart(){
        if (mGetRotationChart != null && !mGetRotationChart.isFinish()) {
            return;
        }
        mGetRotationChart=new GetRotationChart();
        mGetRotationChart.addUrlParam("userName", AccountManager.getUserName());
        mGetRotationChart.setRequestListener(new RequestListener<RotationChartData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(RotationChartData result) {
                images=result.image;
                banner.setImageLoader(new GlideImageLoader());
                banner.setImages(images);
                banner.setDelayTime(2000);
                banner.start();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetRotationChart);
    }
    //绘制柱状图函数
    private void barChart1() {
        String[] xLabel = {"",ChartX[3],ChartX[2],ChartX[1],ChartX[0]};
        String[] yLabel = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int[] data1 = {Chartdata[3],Chartdata[2],Chartdata[1],Chartdata[0]};
        List<int[]> data = new ArrayList<>();
        data.add(data1);
        List<Integer> color = new ArrayList<>();
        color.add(R.color.colorAccent);
        color.add(R.color.colorPrimary);
        color.add(R.color.blue);
        mChartContainer.addView(new CustomBarChart1(mContext, xLabel, yLabel, data, color));
    }
    private void barChart2() {
        String[] xLabel = {"",ChartX[3],ChartX[2],ChartX[1],ChartX[0]};
        String[] yLabel = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900"};
        int[] data1 = {Chartdata[3],Chartdata[2],Chartdata[1],Chartdata[0]};
        List<int[]> data = new ArrayList<>();
        data.add(data1);
        List<Integer> color = new ArrayList<>();
        color.add(R.color.colorAccent);
        color.add(R.color.colorPrimary);
        color.add(R.color.blue);
        mChartContainer.addView(new CustomBarChart(mContext, xLabel, yLabel, data, color));
    }
    private void barChart3() {
        String[] xLabel = {"",ChartX[3],ChartX[2],ChartX[1],ChartX[0]};
        String[] yLabel = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900"};
        int[] data1 = {Chartdata[3],Chartdata[2],Chartdata[1],Chartdata[0]};
        List<int[]> data = new ArrayList<>();
        data.add(data1);
        List<Integer> color = new ArrayList<>();
        color.add(R.color.colorAccent);
        color.add(R.color.colorPrimary);
        color.add(R.color.blue);
        mChartContainer.addView(new CustomBarChart(mContext, xLabel, yLabel, data, color));
    }
}
