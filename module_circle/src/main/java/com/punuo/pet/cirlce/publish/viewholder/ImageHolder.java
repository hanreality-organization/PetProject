package com.punuo.pet.cirlce.publish.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.punuo.pet.cirlce.R;
import com.punuo.pet.cirlce.R2;
import com.punuo.pet.cirlce.publish.helpclass.ICallBack;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kuiya on 2019/8/8.
 */

public class ImageHolder extends BaseViewHolder<String> {


    ICallBack mCallBack;
    @BindView(R2.id.publish_image)
    ImageView mImage;

    public ImageHolder(Context context, ViewGroup parent, ICallBack callBack) {
        super(LayoutInflater.from(context).inflate(R.layout.publish_item_image, parent, false));
        ButterKnife.bind(this, itemView);
        mCallBack = callBack;
    }

    @Override
    protected void bindData(final String path, final int position) {
        if (!TextUtils.equals("add", path)) {
            if (path != null) {
                Glide.with(itemView.getContext()).load(path).into(mImage);
                mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

        else {
            mImage.setImageResource(R.drawable.icon_addpic_unfocused);
        }
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.itemClick(path, position);
                }
            }
        });

    }
}
