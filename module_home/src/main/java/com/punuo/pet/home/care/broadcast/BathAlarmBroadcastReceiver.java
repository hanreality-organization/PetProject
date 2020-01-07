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
import com.punuo.pet.home.care.item_activity.BeautyActivity;
import com.punuo.pet.home.care.item_activity.BuyFoodActivity;
import com.punuo.pet.home.care.item_activity.CheckupActivity;
import com.punuo.pet.home.care.item_activity.DewormingVitroActivity;
import com.punuo.pet.home.care.item_activity.DewormingVivoActivity;
import com.punuo.pet.home.care.item_activity.VaccineActivity;
import com.punuo.pet.home.care.item_activity.WalkActivity;
import com.punuo.sys.sdk.Constant;

public class BathAlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 关于洗澡的alarm
         */
        if (intent.getAction().equals(Constant.ALARM_ONE)) {
            Log.i("bath", "收到洗澡的alarm");
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
                BathActivity.mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.bathDateAndTime+AlarmManager.INTERVAL_DAY*BathActivity.day,BathActivity.pendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                Log.i("bath", "收到洗澡的alarm");
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
                BathActivity.mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.bathDateAndTime+AlarmManager.INTERVAL_DAY*BathActivity.day,BathActivity.pendingIntent);
            }
        }
        /**
         * 关于体检的alarm
         */
        if (intent.getAction().equals(Constant.ALARM_TWO)) {
            Log.i("check", "收到体检的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体检");
                    builder.setContentText("您的爱宠需要体检啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, CheckupActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("check", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体检");
                    builder.setContentText("您的爱宠需要体检啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, CheckupActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("check", "消息推送啦!!");
                }
                CheckupActivity.checkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.checkDateAndTime+ AlarmManager.INTERVAL_DAY*CheckupActivity.day,CheckupActivity.checkPendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                Log.i("check", "收到体检的alarm");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体检");
                    builder.setContentText("您的爱宠需要体检啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, CheckupActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("check", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体检");
                    builder.setContentText("您的爱宠需要体检啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, CheckupActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("check", "消息推送啦!!");
                }
                CheckupActivity.checkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.checkDateAndTime+AlarmManager.INTERVAL_DAY*CheckupActivity.day,CheckupActivity.checkPendingIntent);
            }
        }

        /**
         * 关于买宠物粮
         */
        if (intent.getAction().equals(Constant.ALARM_THREE)) {
            Log.i("buyFood", "收到买宠物粮的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("买宠物粮");
                    builder.setContentText("您需要买宠物粮啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, BuyFoodActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("buyFood", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("买宠物粮");
                    builder.setContentText("您需要买宠物粮啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, BuyFoodActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("buyFood", "消息推送啦!!");
                }
                BuyFoodActivity.buyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.buyDateAndTime+ AlarmManager.INTERVAL_DAY*BuyFoodActivity.day,BuyFoodActivity.buyPendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                Log.i("buyFood", "收到买宠物粮的alarm");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("买宠物粮");
                    builder.setContentText("您需要买宠物粮啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, BuyFoodActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("buyFood", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("买宠物粮");
                    builder.setContentText("您需要买宠物粮啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, BuyFoodActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("buyFood", "消息推送啦!!");
                }
                BuyFoodActivity.buyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.buyDateAndTime+AlarmManager.INTERVAL_DAY*BuyFoodActivity.day,BuyFoodActivity.buyPendingIntent);
            }
        }

        /**
         * 关于体内驱虫
         */
        if (intent.getAction().equals(Constant.ALARM_FOUR)) {
            Log.i("inner", "收到体内驱虫的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体内驱虫");
                    builder.setContentText("您的爱宠需要体内驱虫啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, DewormingVivoActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("inner", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体内驱虫");
                    builder.setContentText("您的宠物需要体内驱虫啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, DewormingVivoActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("inner", "消息推送啦!!");
                }
                DewormingVivoActivity.innerAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.vivoDateAndTime+ AlarmManager.INTERVAL_DAY*DewormingVivoActivity.day,DewormingVivoActivity.innerPendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                Log.i("inner", "收到体内驱虫的alarm");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体内驱虫");
                    builder.setContentText("您的爱宠需要体内驱虫啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, DewormingVivoActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("inner", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体内驱虫");
                    builder.setContentText("您的爱宠需要体内驱虫啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, DewormingVivoActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("inner", "消息推送啦!!");
                }
                DewormingVivoActivity.innerAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.vivoDateAndTime+AlarmManager.INTERVAL_DAY*DewormingVivoActivity.day,DewormingVivoActivity.innerPendingIntent);
            }
        }

        /**
         *关于体外驱虫
         */
        if (intent.getAction().equals(Constant.ALARM_FIVE)) {
            Log.i("outer", "收到体外驱虫的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体外驱虫");
                    builder.setContentText("您的爱宠需要体外驱虫啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, DewormingVitroActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("outer", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体外驱虫");
                    builder.setContentText("您的宠物需要体外驱虫啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, DewormingVitroActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("outer", "消息推送啦!!");
                }
                DewormingVitroActivity.outerAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.vitroDateAndTime+ AlarmManager.INTERVAL_DAY*DewormingVitroActivity.day,DewormingVitroActivity.outerPendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                Log.i("outer", "收到体外驱虫的alarm");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体外驱虫");
                    builder.setContentText("您的爱宠需要体外驱虫啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, DewormingVitroActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("outer", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("体外驱虫");
                    builder.setContentText("您的爱宠需要体外驱虫啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, DewormingVitroActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("inner", "消息推送啦!!");
                }
                DewormingVitroActivity.outerAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.vitroDateAndTime+AlarmManager.INTERVAL_DAY*DewormingVitroActivity.day,DewormingVitroActivity.outerPendingIntent);
            }
        }

        /**
         * 疫苗注射
         */
        if (intent.getAction().equals(Constant.ALARM_SIX)) {
            Log.i("vaccine", "收到疫苗注射的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("疫苗注射");
                    builder.setContentText("您的爱宠需要疫苗注射啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, VaccineActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("vaccine", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("疫苗注射");
                    builder.setContentText("您的宠物需要疫苗注射啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, VaccineActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("vaccine", "消息推送啦!!");
                }
                VaccineActivity.vaccineAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.vaccineDateAndTime+ AlarmManager.INTERVAL_DAY*VaccineActivity.day,VaccineActivity.vaccinePendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("疫苗注射");
                    builder.setContentText("您的爱宠需要疫苗注射啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, VaccineActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("vaccine", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("疫苗注射");
                    builder.setContentText("您的爱宠需要疫苗注射啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, VaccineActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("vaccine", "消息推送啦!!");
                }
                VaccineActivity.vaccineAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.vaccineDateAndTime+AlarmManager.INTERVAL_DAY*VaccineActivity.day,VaccineActivity.vaccinePendingIntent);
            }
        }

        /**
         * 关于美容护理
         */
        if (intent.getAction().equals(Constant.ALARM_SEVEN)) {
            Log.i("beauty", "收到美容护理的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("美容护理");
                    builder.setContentText("您的爱宠需要美容护理啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, BeautyActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("beauty", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("美容护理");
                    builder.setContentText("您的宠物需要美容护理啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, BeautyActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("beauty", "消息推送啦!!");
                }
                BeautyActivity.beautyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.beautyDateAndTime+ AlarmManager.INTERVAL_DAY*BeautyActivity.day,BeautyActivity.beautyPendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("美容护理");
                    builder.setContentText("您的爱宠需要美容护理啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, BeautyActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("beauty", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("美容护理");
                    builder.setContentText("您的爱宠需要美容护理啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, BeautyActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("beauty", "消息推送啦!!");
                }
                BeautyActivity.beautyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.beautyDateAndTime+AlarmManager.INTERVAL_DAY*BeautyActivity.day,BeautyActivity.beautyPendingIntent);
            }
        }

        /**
         * 关于遛宠
         */
        if (intent.getAction().equals(Constant.ALARM_EIGHT)) {
            Log.i("walk", "收到遛宠的alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("遛宠");
                    builder.setContentText("您的爱宠需要遛宠啦！");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, WalkActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("walk", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("遛宠");
                    builder.setContentText("您的宠物需要遛宠啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, WalkActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("walk", "消息推送啦!!");
                }
                WalkActivity.walkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Constant.walkDateAndTime+ AlarmManager.INTERVAL_DAY*WalkActivity.day,WalkActivity.walkPendingIntent);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("遛宠");
                    builder.setContentText("您的爱宠需要遛宠啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    builder.setChannelId(context.getPackageName());
                    Intent mIntent = new Intent(context, WalkActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    NotificationChannel channel = new NotificationChannel(context.getPackageName(), "会话消息", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(0, builder.build());
                    Log.i("walk", "消息推送啦!");
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentTitle("遛宠");
                    builder.setContentText("您的爱宠需要遛宠啦!");
                    builder.setPriority(NotificationCompat.PRIORITY_MAX);
                    builder.setSmallIcon(R.drawable.bg_bath);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    builder.setAutoCancel(true);
                    Intent mIntent = new Intent(context, WalkActivity.class);
                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(mPendingIntent);
                    manager.notify(0, builder.build());
                    Log.i("walk", "消息推送啦!!");
                }
                WalkActivity.walkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,Constant.walkDateAndTime+AlarmManager.INTERVAL_DAY*WalkActivity.day,WalkActivity.walkPendingIntent);

            }
        }
    }
}
