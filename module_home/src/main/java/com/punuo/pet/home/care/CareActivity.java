package com.punuo.pet.home.care;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.punuo.pet.home.care.request.GetCareRequest;
import com.punuo.pet.model.PetData;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.ToastUtils;

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
        initGridView();

//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        mCareList.setLayoutManager(layoutManager);
//        String careDefault = CommonUtil.getAssetsData("care_default.json");
//        CareModel careModel = JsonUtil.fromJson(careDefault, CareModel.class);
//        mCareAdapter = new CareAdapter(this, careModel == null ? new ArrayList<CareData>() : careModel.mCareDataList);
//        mCareList.setAdapter(mCareAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mCareList.setLayoutManager(layoutManager);
        mCareAdapter = new CareAdapter(this,new ArrayList<CareData>());
        mCareList.setAdapter(mCareAdapter);
    }

    private GetCareRequest mGetCareRequest;
    private void initGridView(){
        if (mGetCareRequest!=null&&!mGetCareRequest.isFinish()){
            return;
        }
        mGetCareRequest = new GetCareRequest();
        mGetCareRequest.addUrlParam("username", AccountManager.getUserName());
        mGetCareRequest.addUrlParam("petname","王斌弟弟");
        mGetCareRequest.setRequestListener(new RequestListener<CareModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(CareModel result) {
                if (result==null){
                    ToastUtils.showToast("获取到的数据为空");
                }
                Log.i("care", "成功获取到care数据 ");
                mCareAdapter.clear();
                mCareAdapter.addAll(result.mCareDataList);
                mCareAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetCareRequest);
    }
}
