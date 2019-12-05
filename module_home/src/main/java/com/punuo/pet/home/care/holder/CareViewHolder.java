package com.punuo.pet.home.care.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.care.model.CareData;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.TimeUtils;
import com.punuo.sys.sdk.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class CareViewHolder extends BaseViewHolder<CareData> {

    @BindView(R2.id.care_icon)
    ImageView mCareIcon;
    @BindView(R2.id.care_name)
    TextView mCareName;
    @BindView(R2.id.care_pet_name)
    TextView mCarePetName;
    @BindView(R2.id.care_date)
    TextView mCareDate;

    public CareViewHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.home_recycler_care_item, parent, false));
        ButterKnife.bind(this, itemView);
        int screenWidth = CommonUtil.getWidth();
        itemView.getLayoutParams().width = (screenWidth - CommonUtil.dip2px(3) * 2) / 2;
    }

    @Override
    protected void bindData(CareData careData, int position) {
//        Glide.with(itemView.getContext()).load(careData.icon).into(mCareIcon);
//        ViewUtil.setText(mCareName, careData.label);
        ViewUtil.setText(mCarePetName, careData.petName);
        ViewUtil.setText(mCareDate, TimeUtils.formatMills(careData.date * 1000));
    }
}
