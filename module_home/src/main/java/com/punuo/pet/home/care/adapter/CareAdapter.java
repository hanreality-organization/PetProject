package com.punuo.pet.home.care.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.punuo.pet.home.R;
import com.punuo.pet.home.care.model.CareData;
import com.punuo.pet.model.PetData;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class CareAdapter extends BaseRecyclerViewAdapter<CareData> {

    private List<CareData> mCareDataList;
    private PetData mPetData;

    public CareAdapter(Context context, List<CareData> data, PetData petData) {
        super(context, data);
        mCareDataList = data;
        mPetData = petData;
    }

    public static class CareHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView careName;

        public CareHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.care_icon);
            careName = (TextView) view.findViewById(R.id.care_name);
        }

        public void bindData(CareData data, PetData mPetData) {
            if (data == null) {
                return;
            }
            careName.setText(data.getCareName());
            Glide.with(itemView.getContext()).load(data.getIcon()).into(icon);
            itemView.setOnClickListener(v -> {
                ARouter.getInstance().build(HomeRouter.ROUTER_CARE_DETAIL_ACTIVITY)
                        .withString("title", data.getCareName())
                        .withString("theme", data.getTheme())
                        .withParcelable("petData", mPetData)
                        .navigation();
            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_care_item,
                parent, false);
        return new CareHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        CareData careData = mCareDataList.get(position);
        if (baseViewHolder instanceof CareHolder) {
            ((CareHolder) baseViewHolder).bindData(careData, mPetData);
        }
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    @Override
    public int getBasicItemCount() {
        return mCareDataList == null ? 0 : mCareDataList.size();
    }
}
