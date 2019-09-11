package com.punuo.pet.home.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.bluetooth.model.BaseInfo;
import com.punuo.pet.home.bluetooth.model.BluetoothInfo;
import com.punuo.pet.home.bluetooth.model.BluetoothLabel;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-13.
 **/
public class BluetoothInfoAdapter extends BaseRecyclerViewAdapter<BaseInfo> {

    public interface OnClickListener {
        void click(BluetoothInfo bluetoothInfo);
    }

    public interface OnLongClickListener {
        void click(BluetoothInfo bluetoothInfo);
    }

    private OnClickListener itemClickListener;
    private OnLongClickListener mLongClickListener;

    public BluetoothInfoAdapter(Context context, List<BaseInfo> data) {
        super(context, data);
    }

    public void setItemClickListener(OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setLongClickListener(OnLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseInfo.TYPE_LABEL:
                return new BluetoothLabelVH(mContext, parent);
            case BaseInfo.TYPE_BLUETOOTH_MARCHED:
                return new BluetoothInfoVH(mContext, parent, itemClickListener, mLongClickListener);
            case BaseInfo.TYPE_BLUETOOTH_FIND:
                return new BluetoothInfoVH(mContext, parent, itemClickListener, null);
            default:
                return new BluetoothLabelVH(mContext, parent);
        }
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        BaseInfo baseInfo = mData.get(position);
        if (baseViewHolder instanceof BluetoothInfoVH) {
            if (baseInfo instanceof BluetoothInfo) {
                ((BluetoothInfoVH) baseViewHolder).bind((BluetoothInfo) baseInfo, position);
            }
        } else if (baseViewHolder instanceof BluetoothLabelVH) {
            if (baseInfo instanceof BluetoothLabel) {
                ((BluetoothLabelVH) baseViewHolder).bind((BluetoothLabel) baseInfo, position);
            }
        }
    }

    @Override
    public int getBasicItemType(int position) {
        BaseInfo baseInfo = mData.get(position);
        return baseInfo.cellType;
    }

    @Override
    public int getBasicItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class BluetoothInfoVH extends BaseViewHolder<BluetoothInfo> {

        @BindView(R2.id.item_bluetooth_type)
        ImageView mItemBluetoothType;
        @BindView(R2.id.item_name_info)
        TextView mItemNameInfo;
        @BindView(R2.id.item_address_info)
        TextView mItemAddressInfo;
        private Context mContext;
        private OnClickListener itemClickListener;
        private OnLongClickListener mLongClickListener;

        public BluetoothInfoVH(Context context, ViewGroup parent,
                               OnClickListener clickListener, OnLongClickListener longClickListener) {
            super(LayoutInflater.from(context).inflate(R.layout.home_recycler_bluetooth_item,
                    parent, false));
            mContext = context;
            itemClickListener = clickListener;
            mLongClickListener = longClickListener;
            ButterKnife.bind(this, itemView);

        }

        @Override
        protected void bindData(final BluetoothInfo bluetoothInfo, int position) {
            final BluetoothDevice bluetoothDevice = bluetoothInfo.bluetoothDevice;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.click(bluetoothInfo);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickListener != null) {
                        mLongClickListener.click(bluetoothInfo);
                    }
                    return true;
                }
            });

            if (bluetoothInfo.isConnect) {
                mItemBluetoothType.setImageResource(R.drawable.home_ic_mobile_select);
                mItemNameInfo.setText("(已连接)" + bluetoothDevice.getName());
            } else {
                mItemBluetoothType.setImageResource(R.drawable.home_ic_mobile_un_select);
                mItemNameInfo.setText(bluetoothDevice.getName());
            }
            mItemAddressInfo.setText(bluetoothDevice.getAddress());
        }
    }

    public static class BluetoothLabelVH extends BaseViewHolder<BluetoothLabel> {

        @BindView(R2.id.home_bluetooth_label)
        TextView mHomeBluetoothLabel;

        public BluetoothLabelVH(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.home_recycler_bluetooth_label,
                    parent, false));
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void bindData(BluetoothLabel bluetoothLabel, int position) {
            mHomeBluetoothLabel.setText(bluetoothLabel.label);
        }
    }
}
