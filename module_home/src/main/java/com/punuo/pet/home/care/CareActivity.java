package com.punuo.pet.home.care;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.care.adapter.CareAdapter;
import com.punuo.pet.home.care.model.CareData;
import com.punuo.pet.home.care.model.CareModel;
import com.punuo.pet.model.PetData;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.JsonUtil;
import com.punuo.sys.sdk.util.CommonUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-06.
 **/
@Route(path = HomeRouter.ROUTER_CARE_ACTIVITY)
public class CareActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.care_list)
    RecyclerView mCareList;

    @Autowired(name = "petData")
    PetData mPetData;

    private CareAdapter mCareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_care);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitle.setText("日常护理");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mCareList.setLayoutManager(layoutManager);
        String careDefault = CommonUtil.getAssetsData("care_default.json");
        CareModel careModel = JsonUtil.fromJson(careDefault, CareModel.class);
        mCareAdapter = new CareAdapter(this, careModel == null ?
                new ArrayList<CareData>() : careModel.mCareDataList);
        mCareList.setAdapter(mCareAdapter);
    }
}
