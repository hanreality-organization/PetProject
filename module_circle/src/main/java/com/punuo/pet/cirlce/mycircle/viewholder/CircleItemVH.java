package com.punuo.pet.cirlce.mycircle.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punuo.pet.cirlce.R;
import com.punuo.pet.cirlce.R2;
import com.punuo.pet.cirlce.mycircle.model.CircleItem;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
public class CircleItemVH extends BaseViewHolder<CircleItem> {



//    @BindView(R2.id.item_text)
//    TextView mItemText;

    public CircleItemVH(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.circle_item_layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindData(CircleItem circleItem, int position) {
//        mItemText.setText("item " + position);
    }
}
