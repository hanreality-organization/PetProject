package com.punuo.sys.app.video.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.model.VideoItem;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mPathList;

    public VideoAdapter(Context context,List<String> pathList){
        mContext=context;
        mPathList=pathList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(mContext).inflate(R.layout.video_item,parent,false);
        return new VideoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String path=mPathList.get(position);
        holder.mVideoView.setVisibility(View.VISIBLE);
        holder.mVideoView.setVideoPath(path);
        holder.mVideoView.start();
    }

    @Override
    public int getItemCount() {
        return mPathList==null ? 0 :mPathList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
       VideoView mVideoView;
       public ViewHolder(View itemView){
           super(itemView);
           mVideoView=itemView.findViewById(R.id.videoView);
       }
    }
}
