package com.punuo.sip.dev;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipOnLineRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by han.chen.
 * Date on 2020/9/5.
 **/
public class DevManager {
    private static DevManager sInstance;
    public static DevManager getInstance() {
        if (sInstance == null) {
            synchronized (DevManager.class) {
                if (sInstance == null) {
                    sInstance = new DevManager();
                }
            }
        }
        return sInstance;
    }

    private String devId;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
       this.devId = devId;
    }

    public boolean isBindDevice() {
        return !TextUtils.isEmpty(devId);
    }

    private boolean isHost = false;

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isHost() {
        return isHost;
    }

    private boolean isOnline = false;

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean online() {
        return isOnline;
    }

    public void refreshDevRelationShip() {
        final GetDevIdRequest request = new GetDevIdRequest();
        request.addUrlParam("userName", AccountManager.getUserName());
        request.setRequestListener(new RequestListener<DevData>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(DevData result) {
                if (result == null) {
                    devId = "";
                    return;
                }
                if (!TextUtils.isEmpty(result.devId)) {
                    devId = result.devId;
                } else  {
                    devId = "";
                }
                DevManager.getInstance().isOnline();
                checkHostOfSelf();
            }

            @Override
            public void onError(Exception e) {
                devId = "";
            }
        });
        HttpManager.addRequest(request);
    }

    /**
     * 获取设备是否在线
     */
    public void isOnline() {
        SipOnLineRequest sipOnLineRequest = new SipOnLineRequest();
        SipUserManager.getInstance().addRequest(sipOnLineRequest);
    }

    private CheckSelfIsHostRequest checkHostOfSelfRequest;
    private void checkHostOfSelf() {
        if (checkHostOfSelfRequest != null && !checkHostOfSelfRequest.isFinished) {
            checkHostOfSelfRequest.finish();
        }
        checkHostOfSelfRequest = new CheckSelfIsHostRequest();
        checkHostOfSelfRequest.addUrlParam("userName", AccountManager.getUserName());
        checkHostOfSelfRequest.addUrlParam("devId", DevManager.getInstance().devId);
        checkHostOfSelfRequest.setRequestListener(new RequestListener<SelfHost>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(SelfHost result) {
                if (result == null) {
                    return;
                }
                if (result.getData() != null) {
                    setHost(result.getData().getHost());
                }
                EventBus.getDefault().post(result);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(checkHostOfSelfRequest);
    }

    public void clearDevConfirm(Context context, int leave) {
        AlertDialog dialog = new  AlertDialog.Builder(context)
                .setTitle("温馨提醒")
                .setMessage("是否需要将设备" + DevManager.getInstance().getDevId() +"恢复置出厂设置?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearDev(context, leave);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void clearDev(Context context, int leave) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showLoadingDialog("正在恢复...");
        }
        ClearDevRequest clearDevRequest = new ClearDevRequest();
        clearDevRequest.addUrlParam("username", AccountManager.getUserName());
        clearDevRequest.addUrlParam("devid", DevManager.getInstance().getDevId());
        clearDevRequest.addUrlParam("leave", leave);
        clearDevRequest.setRequestListener(new RequestListener<ClearDevEvent>() {
            @Override
            public void onComplete() {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).dismissLoadingDialog();
                }
            }

            @Override
            public void onSuccess(ClearDevEvent result) {
                if (result != null) {
                    ToastUtils.showToast(result.message);
                    if (result.success) {
                        DevManager.getInstance().refreshDevRelationShip();
                        EventBus.getDefault().post(result);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(clearDevRequest);
    }
}
