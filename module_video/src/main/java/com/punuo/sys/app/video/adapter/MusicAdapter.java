package com.punuo.sys.app.video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipSendMusicRequest;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.holder.MusicHolder;
import com.punuo.sys.app.video.model.MusicItem;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
public class MusicAdapter extends BaseRecyclerViewAdapter<MusicItem> {

    public MusicAdapter(Context context, List<MusicItem> data) {
        super(context, data);
    }

    public void addMusicList(List<String> urls) {
        mData.clear();
        for (int i = 0; i < urls.size(); i++) {
            MusicItem musicItem = new MusicItem();
            musicItem.url = urls.get(i);
            mData.add(musicItem);
        }
        notifyDataSetChanged();
    }

    public void reset() {
        for (int i = 0; i < mData.size(); i++) {
            MusicItem musicItem = mData.get(i);
            musicItem.selected = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MusicHolder(LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false));
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, final int position) {
        if (baseViewHolder instanceof MusicHolder) {
            ((MusicHolder) baseViewHolder).bind(mData.get(position), position);
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MusicItem item = mData.get(position);
                    if (item.selected) {
                        return;
                    }
                    for (int i = 0; i < mData.size(); i++) {
                        MusicItem musicItem = mData.get(i);
                        musicItem.selected = i == position;
                    }
                    notifyDataSetChanged();
                    sendDevMusic(mData.get(position).url);
                }
            });
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

    private void sendDevMusic(String musicUrl) {
        SipSendMusicRequest sipSendMusicRequest
                = new SipSendMusicRequest("310023001301920001", musicUrl);
        SipUserManager.getInstance().addRequest(sipSendMusicRequest);
    }


}
