package com.punuo.pet.home.care.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punuo.pet.home.R;
import com.punuo.pet.model.PetData;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

public class PetRelevanceAdapter extends BaseRecyclerViewAdapter {

    private List<PetData> mPetList;
    private OnItemClickListener onItemClickListener;

    public PetRelevanceAdapter(Context context, List<PetData> data){
        super(context,data);
        mPetList = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list_item,parent,false);
        final PetListViewHolder holder = new PetListViewHolder(view);
        holder.petView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutPosition = holder.getLayoutPosition();
                if (onItemClickListener != null){
                    onItemClickListener.OnItemClick(layoutPosition);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        PetData petData = mPetList.get(position);
        if (baseViewHolder instanceof PetListViewHolder){
            ((PetListViewHolder) baseViewHolder).petName.setText(petData.petname);
        }

    }

    @Override
    public int getBasicItemCount() {
        return mPetList == null? 0:mPetList.size();
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }


    static class PetListViewHolder extends RecyclerView.ViewHolder{
        View petView;
        TextView petName;

        public  PetListViewHolder(View view){
         super(view);
         petView = view;
         petName = view.findViewById(R.id.pet_name);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListenrt(OnItemClickListener onItemClickListenrt){
        this.onItemClickListener = onItemClickListenrt;
    }

    public String getPetName(int position){
        return mPetList.get(position).petname;
    }
}
