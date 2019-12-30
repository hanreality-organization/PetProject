package com.punuo.pet.home.care.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.punuo.pet.home.R;
import com.punuo.pet.home.care.holder.CareViewHolder;
import com.punuo.pet.home.care.model.CareData;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class CareAdapter extends BaseRecyclerViewAdapter<CareData> {

    private List<CareData> mCareDataList;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault());

    public CareAdapter(Context context,List<CareData> data) {
        super(context,data);
        mCareDataList  = data;
    }
    static class CareHolder extends RecyclerView.ViewHolder{
        View careView;
        ImageView icon;
        TextView careName;
        TextView petName;
        TextView careDate;
//        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault());

        public CareHolder(View view){
            super(view);
            careView = view;
            icon = (ImageView)view.findViewById(R.id.care_icon);
            careName = (TextView)view.findViewById(R.id.care_name);
            petName = (TextView)view.findViewById(R.id.care_pet_name);
            careDate = (TextView)view.findViewById(R.id.care_date);
        }

//        public void bindData(CareData careData) {
//            Date date = new Date(careData.getDate()*1000);
//            careName.setText(careData.careName);
//            petName.setText(careData.getPetName());
//            careDate.setText(mSimpleDateFormat.format(date));
//            ImageLoader.getInstance().displayImage("http://pet.qinqingonline.com"+);
//        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_care_item,parent,false);
        final CareHolder holder = new CareHolder(view);
        holder.careView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Log.i("care", "点击的位置"+position);
                if (position==0){ ARouter.getInstance().build(HomeRouter.ROUTER_CARE_BATH_ACTIVITY).navigation();
                }else if (position==1){ ARouter.getInstance().build(HomeRouter.ROUTER_CARE_CHECKUP_ACTIVITY).withParcelable("petData", Constant.petData).navigation();
                }else if (position==2){ARouter.getInstance().build(HomeRouter.ROUTER_CARE_BUY_FOOD_ACTIVITY).navigation();
                }else if (position==3){ARouter.getInstance().build(HomeRouter.ROUTER_CARE_IN_VIVO_ACTIVITY).navigation();
                }else if (position==4){ARouter.getInstance().build(HomeRouter.ROUTER_CARE_IN_VITRO_ACTIVITY).navigation();
                }else if (position==5){ARouter.getInstance().build(HomeRouter.ROUTER_CARE_VACCINE_ACTIVITY).navigation();
                }else if (position==6){ARouter.getInstance().build(HomeRouter.ROUTER_CARE_BEAUTY_ACTIVITY).navigation();
                }else if (position==7){ARouter.getInstance().build(HomeRouter.ROUTER_CARE_WALK_ACTIVITY).navigation();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        CareData careData = mCareDataList.get(position);
        if(baseViewHolder instanceof CareHolder) {
//            ((CareHolder) baseViewHolder).bindData(careData); }
            Date date = new Date(careData.getDate()*1000);
            ((CareHolder) baseViewHolder).careName.setText(careData.careName);
            ((CareHolder) baseViewHolder).petName.setText(careData.getPetName());
            ((CareHolder) baseViewHolder).careDate.setText(mSimpleDateFormat.format(date));
//            ImageLoader.getInstance().displayImage(mCareDataList.get(position).icon,((CareHolder) baseViewHolder).icon);
            Log.i("icon", ""+mCareDataList.get(position).icon);
            Glide.with(mContext).load(mCareDataList.get(position).icon).into(((CareHolder) baseViewHolder).icon);
        }
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    @Override
    public int getBasicItemCount() {
        return mCareDataList==null? 0:mCareDataList.size();
    }
}
