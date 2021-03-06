package com.punuo.pet.update;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.sys.sdk.R;
import com.punuo.sys.sdk.httplib.JsonUtil;
import com.punuo.sys.sdk.httplib.StringRequest;
import com.punuo.sys.sdk.util.DeviceHelper;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhoukuan on 14/12/31.
 */
public class AutoUpdateService extends Service {

    private static AutoUpdateService instance;
    private boolean downloading;
    private boolean needToast = false;
    private ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static AutoUpdateService getInstance() {
        return instance;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        VersionModel versionModel = null;

        if (intent != null) {
            versionModel = intent.getParcelableExtra("versionModel");
            needToast = intent.getBooleanExtra("needToast", false);
        }
        if (!isDownloading()) {
            if (versionModel != null) {
                download(versionModel, true);
            } else {
                new CheckForUpdateTask().executeOnExecutor(mExecutor);
            }
        } else {
            ToastUtils.showToast(getString(R.string.string_downloading));
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    private class CheckForUpdateTask extends AsyncTask<Void, Void, VersionModel> {

        @Override
        protected VersionModel doInBackground(Void... params) {
            try {
                String updateString = new StringRequest("http://pet.qinqingonline.com/latestVersion").execute();
                return JsonUtil.fromJson(updateString, VersionModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(VersionModel versionModel) {
            super.onPostExecute(versionModel);
            if (versionModel == null) return;
            MMKVUtil.setInt("remote_version_code", versionModel.versionCode);
            MMKVUtil.setString("remote_version_name", versionModel.versionName);
            if (versionModel.versionCode > DeviceHelper.getVersionCode(instance)) {
                ARouter.getInstance().build(SDKRouter.ROUTER_UPDATE_DIALOG_ACTIVITY)
                        .withParcelable("versionModel", versionModel)
                        .navigation();
            } else {
                if (needToast) {
                    ToastUtils.showToast(getString(R.string.string_app_update_text));
                }
            }
        }
    }

    private void download(VersionModel info, boolean needAutoInstall) {
        Download download = new Download(info, instance, needAutoInstall);
        new Thread(download).start();
    }
}
