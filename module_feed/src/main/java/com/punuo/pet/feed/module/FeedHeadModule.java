package com.punuo.pet.feed.module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.loonggg.weekcalendar.view.WeekCalendar;
import com.punuo.pet.feed.R;
import com.punuo.pet.feed.R2;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sip.dev.DevManager;
import com.punuo.sys.sdk.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2020-01-07.
 **/
public class FeedHeadModule {

    @BindView(R2.id.pet_container)
    LinearLayout mPetContainer;
    @BindView(R2.id.calendar_week)
    WeekCalendar mCalendarWeek;
    @BindView(R2.id.remainder)
    TextView mRemainder;
    @BindView(R2.id.out)
    TextView mOut;
    @BindView(R2.id.plan)
    TextView mPlan;
    private View mView;
    private Context mContext;
    private String lastRemainder;

    public FeedHeadModule(Context context, ViewGroup parent) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.feed_head_module_layout, parent, false);
        ButterKnife.bind(this, mView);
    }

    public View getView() {
        return mView;
    }

    public void setOnDateClickListener(WeekCalendar.OnDateClickListener listener) {
        mCalendarWeek.setOnDateClickListener(listener);
    }

    public void initPetInfo(PetModel petModel) {
        if (petModel == null || petModel.mPets == null) {
            return;
        }
        mPetContainer.removeAllViews();
        for (int i = 0; i < petModel.mPets.size(); i++) {
            PetData petData = petModel.mPets.get(i);
            View view = LayoutInflater.from(mContext).
                    inflate(R.layout.feed_pet_info_item, mPetContainer, false);
            ImageView avatar = view.findViewById(R.id.pet_avatar);
            TextView petName = view.findViewById(R.id.pet_name);
            Glide.with(mContext).load(petData.avatar).into(avatar);
            ViewUtil.setText(petName, petData.petname);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(MemberRouter.ROUTER_PET_INFO_ACTIVITY)
                            .withParcelable("petData", petData)
                            .withBoolean("canEdit", false)
                            .navigation();
                }
            });
            mPetContainer.addView(view);
        }
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.feed_add_item, mPetContainer, false);
        mPetContainer.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_PET_INFO_ACTIVITY)
                        .withBoolean("canEdit", true)
                        .navigation();
            }
        });
    }

    public void resetDisplay() {
        mRemainder.setText("0.0");
        mOut.setText("0");
    }

    public void updateRemainder(String remainder, boolean needCheck) {
        mRemainder.setText(remainder);
        if (needCheck) {
            checkRemainder(remainder);
        }
    }

    private void checkRemainder(String remainder) {
        int remainderInt = Integer.parseInt(remainder);
        if (remainderInt < 100 && DevManager.getInstance().online()) {
            showAlterDialog();
        }
    }

    public void updatePlan(String feedCountSum) {
        mPlan.setText(feedCountSum);
    }

    public void updateOutCount(int outedCount) {
        mOut.setText(String.valueOf(outedCount));
    }

    private void showAlterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("亲，余粮不足啦！");
        dialog.setMessage("余粮不够了亲，请及时补充粮食。如果您已经补充过，请您按一下界面下方的“加粮完毕”");
        dialog.setPositiveButton("知道啦！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onVisible() {
        mCalendarWeek.refreshTime();
    }
}
