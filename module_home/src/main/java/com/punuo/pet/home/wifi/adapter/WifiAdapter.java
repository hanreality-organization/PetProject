package com.punuo.pet.home.wifi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.wifi.WifiUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-14.
 **/
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder> {

    private Context mContext;
    private List<ScanResult> mData;
    private onItemClickListener mItemClickListener;

    public WifiAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void clear() {
        mData.clear();
    }

    public void addAll(List<ScanResult> data) {
        clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiAdapter.WifiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WifiViewHolder(mContext, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiAdapter.WifiViewHolder viewHolder, int i) {
        viewHolder.bindData(mData.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class WifiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.wifi_pwd)
        ImageView mWifiPwd;
        @BindView(R2.id.wifi_name)
        TextView mWifiName;
        @BindView(R2.id.wifi_state)
        TextView mWifiState;

        public WifiViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.home_recycler_wifi_item, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final ScanResult scanResult, int position) {
            mWifiName.setText(scanResult.SSID);
            if (scanResult.capabilities.contains("WPA") || scanResult.capabilities.contains("PSK")) {
                mWifiPwd.setImageResource(R.drawable.home_ic_wifi_pwd);
            } else {
                mWifiPwd.setImageResource(R.drawable.home_ic_wifi_no_pwd);
            }
            if (WifiUtil.getConnectWifiBssid().equals(scanResult.BSSID)) {
                mWifiState.setText("当前连接");
                mWifiState.setTextColor(Color.RED);
            } else {
                mWifiState.setText("");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, getLayoutPosition(), scanResult);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position, ScanResult result);
    }
}
