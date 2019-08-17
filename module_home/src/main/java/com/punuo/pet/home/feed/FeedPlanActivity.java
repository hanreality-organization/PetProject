package com.punuo.pet.home.feed;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.punuo.pet.PetManager;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
@Route(path = HomeRouter.ROUTER_FEED_PLAN_ACTIVITY)
public class FeedPlanActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.pet_container)
    LinearLayout mPetContainer;
    @BindView(R2.id.date_text)
    TextView mDateText;
    @BindView(R2.id.date_select_btn)
    Button mDateSelectBtn;

    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_feed_plan_activity);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
        PetManager.getPetInfo();
    }

    private void initView() {
        mTitle.setText("梦视科技喂食器");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSubTitle.setText("新增喂食计划");
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_ADD_FEED_PLAN_ACTIVITY)
                        .navigation();
            }
        });
        mDateSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatePickerDialog != null && mDatePickerDialog.isShowing()) {
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                mDatePickerDialog = new DatePickerDialog(FeedPlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = getResources().getString(R.string.date, year, month + 1, dayOfMonth);
                        mDateText.setText(date);
                    }
                }, yy, mm, dd);
                mDatePickerDialog.show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        String today = format.format(calendar.getTime());
        mDateText.setText(today);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel petModel) {
        initPetInfo(petModel);
    }

    private void initPetInfo(PetModel petModel) {
        if (petModel == null || petModel.mPets == null) {
            return;
        }
        mPetContainer.removeAllViews();
        for (int i = 0; i < petModel.mPets.size(); i++) {
            PetData petData = petModel.mPets.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.feed_pet_info_item, mPetContainer, false);
            ImageView avatar = view.findViewById(R.id.pet_avatar);
            TextView petName = view.findViewById(R.id.pet_name);
            Glide.with(this).load(petData.avatar).into(avatar);
            ViewUtil.setText(petName, petData.petname);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mPetContainer.addView(view);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.feed_add_item, mPetContainer, false);
        mPetContainer.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
