package com.wkz.framework.managers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import com.wkz.framework.FRApplication;

public class FRNotificationManager {

    private static class Holder {
        private static FRNotificationManager INSTANCE = new FRNotificationManager();
    }

    private FRNotificationManager() {
    }

    public static FRNotificationManager getInstance() {
        return Holder.INSTANCE;
    }

    private NotificationManager mNotificationManager;

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) FRApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    private Notification.Builder getNotificationBuilder(String title, String content, String channelId, @DrawableRes int icon) {
        //大于8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //id随便指定
            NotificationChannel channel = new NotificationChannel(channelId, FRApplication.getContext().getPackageName(), NotificationManager.IMPORTANCE_HIGH);
            channel.canBypassDnd();//可否绕过，请勿打扰模式
            channel.enableLights(true);//闪光
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);//锁屏显示通知
            channel.setLightColor(Color.RED);//指定闪光是的灯光颜色
            channel.canShowBadge();//桌面laucher消息角标
            channel.enableVibration(false);//是否允许震动
            channel.setSound(null, null);
            channel.getAudioAttributes();//获取系统通知响铃声音配置
            channel.getGroup();//获取通知渠道组
            channel.setBypassDnd(true);//设置可以绕过，请勿打扰模式
            channel.setVibrationPattern(new long[]{100, 100, 200});//震动的模式，震3次，第一次100，第二次100，第三次200毫秒
            channel.shouldShowLights();//是否会闪光
            //通知管理者创建的渠道
            getNotificationManager().createNotificationChannel(channel);

            return new Notification.Builder(FRApplication.getContext())
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icon);
        } else {
            return new Notification.Builder(FRApplication.getContext())
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icon);
        }
    }

    public void showNotification(String title, String content, int manageId, String channelId, int progress, int maxProgress, @DrawableRes int icon) {
        Notification.Builder builder = getNotificationBuilder(title, content, channelId, icon);
        builder.setOnlyAlertOnce(true);
        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE);
        builder.setProgress(maxProgress, progress, false);
        builder.setWhen(System.currentTimeMillis());
        //通知管理者发通知
        getNotificationManager().notify(manageId, builder.build());
    }

    public void cancleNotification(int manageId) {
        getNotificationManager().cancel(manageId);
    }
}
