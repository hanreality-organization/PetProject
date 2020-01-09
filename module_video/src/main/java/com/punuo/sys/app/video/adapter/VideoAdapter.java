package com.punuo.sys.app.video.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.punuo.sys.app.video.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mPathList;
    private VideoView mPlayingVideoView;

    public VideoAdapter(Context context, List<String> pathList) {
        mContext = context;
        mPathList = pathList;
    }

    public void addData(List<String> pathList) {
        mPathList.clear();
        if (pathList != null) {
            mPathList.addAll(pathList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new VideoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String path = mPathList.get(position);
        String year=path.substring(56,60);
        String month=path.substring(60,62);
        String day=path.substring(62,64);
        String hour=path.substring(64,66);
        String minute=path.substring(66,68);
        String second=path.substring(68,70);
        holder.CreateTime.setText(year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
        holder.mVideoView.stopPlayback();
        holder.mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayingVideoView!=null){
                    mPlayingVideoView.stopPlayback();
                }
                holder.mVideoView.setVideoPath(path);
                holder.mVideoView.start();
                mPlayingVideoView=holder.mVideoView;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPathList == null ? 0 : mPathList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CreateTime;
        VideoView mVideoView;
        Button mPlayBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            CreateTime=itemView.findViewById(R.id.create_time);
            mVideoView = itemView.findViewById(R.id.videoView);
            mPlayBtn=itemView.findViewById(R.id.play_btn);
        }
    }
}
