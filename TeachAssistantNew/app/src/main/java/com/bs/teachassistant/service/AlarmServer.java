package com.bs.teachassistant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.MainActivity;
import com.bs.teachassistant.entity.ScheBean;

import org.greenrobot.greendao.Property;

/**
 * Created by Administrator on 2017/6/6.
 *
 */

public class AlarmServer extends BroadcastReceiver {
    private static final int ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nmManager;
        nmManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent temp = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, temp, 0);

        Notification.Builder builder = new Notification.Builder(context);//新建Notfication.Builder对象
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle("您有新的日程");
        ScheBean sche = intent.getParcelableExtra("sche");
        builder.setContentText(sche.getTitle() + "|" + sche.getStartTime() + "~" + sche.getEndTime());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pi);

        Notification notification = builder.getNotification();//将builder对象转换为普通的notification
        notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知后通知消失
        Log.e("aa", "aa");

        nmManager.notify(ID, notification);
        //nmManager.notify(ID, notification);

    }
}
