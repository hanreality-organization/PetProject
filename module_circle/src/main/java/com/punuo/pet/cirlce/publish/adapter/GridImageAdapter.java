package com.punuo.pet.cirlce.publish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.publish.helpclass.ICallBack;
import com.punuo.pet.cirlce.publish.viewholder.ImageHolder;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Kuiya on 2019/8/8.
 */

public class GridImageAdapter extends BaseRecyclerViewAdapter{

        private ICallBack mCallBack;
        public GridImageAdapter(Context context, List<String> data, ICallBack callBack) {
            super(context, data);
            mCallBack = callBack;
            mData.add("add");
        }

        public void resetData(List<String> list) {
            mData.clear();
            mData.addAll(list);
            if (list.size() < 9) {
                mData.add("add");
            }
            notifyDataSetChanged();
        }

        public void addData(String image) {
            if (getBasicItemCount() < 8) {
                mData.add(0, image);
            }
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(mContext, parent, mCallBack);
        }

        @Override
        public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
            if (baseViewHolder instanceof ImageHolder) {
                ((ImageHolder) baseViewHolder).bind((String) getItem(position), position);
            }
        }

        @Override
        public int getBasicItemType(int position) {
            return 0;
        }

        @Override
        public int getBasicItemCount() {
            return mData == null ? 0 : mData.size();
        }

}
