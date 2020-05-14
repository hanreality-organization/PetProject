package com.punuo.sys.app.video.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.punuo.sys.app.video.R;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mPathList;

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
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String path = mPathList.get(position);
        String year = path.substring(56, 60);
        String month = path.substring(60, 62);
        String day = path.substring(62, 64);
        String hour = path.substring(64, 66);
        String minute = path.substring(66, 68);
        String second = path.substring(68, 70);
        holder.CreateTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        int width = CommonUtil.getWidth() - CommonUtil.dip2px(20);
        int height = 480 * width / 640;
        holder.mFirstFrame.getLayoutParams().width = width;
        holder.mFirstFrame.getLayoutParams().height = height;
        RequestOptions requestOptions = new RequestOptions()
                .frame(1000000)
                .error(R.drawable.ic_video_error)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop();
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(path)
                .into(holder.mFirstFrame);
        holder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用系统播放器进行播放
                List<String> players = queryPlayerPackageNameLst(mContext, path);
                if (players != null && !players.isEmpty()) {
                    String extension = MimeTypeMap.getFileExtensionFromUrl(path);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
                    mediaIntent.setPackage(players.get(0));
                    mediaIntent.setDataAndType(Uri.parse(path), mimeType);
                    mContext.startActivity(mediaIntent);
                } else {
                    ToastUtils.showToast("未找到可用的播放器");
                }
            }
        });

    }

    private List<String> queryPlayerPackageNameLst(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "video/*");
        PackageManager pm = context.getPackageManager();
        //所有安装的应用(包括已卸载的但目录还存在的)
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_UNINSTALLED_PACKAGES);
        List<String> packageNameLst = null;
        if (infos != null && infos.size() > 0) {
            packageNameLst = new ArrayList<>(infos.size());
            for (int i = 0; i < infos.size(); i++) {
                ResolveInfo info = infos.get(i);
                packageNameLst.add(info.activityInfo.packageName);
            }
        }
        return packageNameLst;
    }

    @Override
    public int getItemCount() {
        return mPathList == null ? 0 : mPathList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CreateTime;
        ImageView mFirstFrame;
        ImageView mPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            mFirstFrame = itemView.findViewById(R.id.video_first);
            CreateTime = itemView.findViewById(R.id.create_time);
            mPlay = itemView.findViewById(R.id.video_play);
        }
    }
}
