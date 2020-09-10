package com.punuo.pet.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.sys.sdk.R;
import com.punuo.sys.sdk.util.FileUtils;
import com.punuo.sys.sdk.util.IntentUtil;


/**
 * https://pet.qinqingonline.com/developers/appUpgrade?versionName=10002&versionCode=3
 * Created by han.chen.
 * Date on 2020/8/3.
 **/
@Route(path = SDKRouter.ROUTER_UPDATE_DIALOG_ACTIVITY)
public class UpdateDialogActivity extends Activity {

    @Autowired(name = "versionModel")
    VersionModel versionModel;

    private TextView mTextNewVersion;
    private Button buttonOk;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        ARouter.getInstance().inject(this);
        setupView();
        setupData();
    }

    public void setupView() {
        mTextNewVersion = (TextView) findViewById(R.id.text_new_version);
        buttonOk = (Button) findViewById(R.id.update_btn_ok);
        buttonCancel = (Button) findViewById(R.id.update_btn_cancel);
    }

    private void setupData() {
        if (versionModel == null) {
            finish();
            return;
        }
        mTextNewVersion.setText("V" + versionModel.versionName);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = versionModel.downloadUrl.substring(versionModel.downloadUrl.lastIndexOf("/"));
                if (FileUtils.isFileExist(FileUtils.DEFAULT_APK_DIR, fileName)) {
                    FileUtils.deleteFile(FileUtils.DEFAULT_APK_DIR, fileName);
                }
                Intent intent = new Intent(UpdateDialogActivity.this, AutoUpdateService.class);
                intent.putExtra("versionModel", versionModel);
                IntentUtil.startServiceInSafeMode(UpdateDialogActivity.this, intent);
                finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AutoUpdateService.getInstance() != null) {
                    AutoUpdateService.getInstance().setDownloading(false);
                }
                finish();
            }
        });
    }

}
