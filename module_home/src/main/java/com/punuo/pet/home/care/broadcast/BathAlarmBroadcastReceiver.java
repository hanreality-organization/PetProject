package com.punuo.pet.home.care.broadcast;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.punuo.pet.home.R;
import com.punuo.pet.home.care.item_activity.BathActivity;
import com.punuo.sys.sdk.Constant;

public class BathAlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 关于洗澡的alarm
         */
        if (intent.getAction().equals(Constant.ALARM_ONE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("洗澡清洁");
                    builder.setContentText("您的爱宠准备洗澡啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, BathActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("bath", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("洗澡清洁");
                    builder.setContentText("您的爱宠准备洗澡啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, BathActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("bath", "消息推送啦!!");
                }
                BathActivity.mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.bathDateAndTime+AlarmManager.INTERVAL_DAY*7,BathActivity.pendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("洗澡清洁");
                    builder.setContentText("您的爱宠准备洗澡啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, BathActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("bath", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("洗澡清洁");
                    builder.setContentText("您的爱宠准备洗澡啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, BathActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("bath", "消息推送啦!!");
                }
                BathActivity.mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.bathDateAndTime+AlarmManager.INTERVAL_DAY*7,BathActivity.pendingIntent);
            }
        }
        /**
         * 关于体检的alarm
         */
    }
}
