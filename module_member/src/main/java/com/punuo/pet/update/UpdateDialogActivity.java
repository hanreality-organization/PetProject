package com.punuo.pet.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.version.VersionModel;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.util.FileUtils;
import com.punuo.sys.sdk.util.IntentUtil;

import java.io.File;


/**
 * Created by han.chen.
 * Date on 2020/8/3.
 **/
@Route(path = MemberRouter.ROUTER_UPDATE_DIALOG_ACTIVITY)
public class UpdateDialogActivity extends Activity {

    @Autowired(name = "versionModel")
    VersionModel versionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        showNewVersionDialog();
    }

    public void showNewVersionDialog() {
        if (versionModel == null) {
            finish();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.activity_update_layout, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_update_msg);
        textView.setText(String.format("新的版本为版本为v%s，确认是否更新?", versionModel.versionName));
        builder.setTitle("温馨提示")
                .setView(v)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = versionModel.downloadUrl.substring(versionModel.downloadUrl.lastIndexOf("/"));
                        if (FileUtils.isFileExist(FileUtils.DEFAULT_APK_DIR, fileName)) {
                            File file = new File(FileUtils.DEFAULT_APK_DIR + File.separator + fileName);
                            try {
                                String[] args1 = {"chmod", "705", file.getPath()};
                                Runtime.getRuntime().exec(args1);
                                String[] args2 = {"chmod", "604", file.getAbsolutePath()};
                                Runtime.getRuntime().exec(args2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Uri uri = Uri.fromFile(file);
                            Intent installIntent = new Intent(Intent.ACTION_VIEW);
                            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                            startActivity(installIntent);
                        } else {
                            Intent intent = new Intent(UpdateDialogActivity.this, AutoUpdateService.class);
                            intent.putExtra("versionModel", versionModel);
                            IntentUtil.startServiceInSafeMode(UpdateDialogActivity.this, intent);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }
}
