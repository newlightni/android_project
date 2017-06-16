package com.bs.teachassistant.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;

import java.util.Calendar;
import java.util.List;

import static com.bs.teachassistant.utils.FormatUtils.formatSuff;

/**
 * Created by Administrator on 2017/6/6.
 */

public class RemindService extends Service {
    private UpdateScheDataBroadcast updateScheDataBroadcast;
    private int year, month, day;
    private List<ScheBean> allSche;
    private SharedPreferences userPreference;

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("Service", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("Service", "onCreate");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_SCHE);
        updateScheDataBroadcast = new UpdateScheDataBroadcast();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateScheDataBroadcast, filter);
        Calendar calendar = Calendar.getInstance();
        userPreference = getSharedPreferences("user", Context.MODE_PRIVATE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        getAlarm();
    }

    public void getAlarm() {
        String today = formatSuff(year) + "/" + formatSuff(month) + "/" + formatSuff(day);
        LogUtil.d("Service", "getAlarm today=" + today);
        if (! TextUtils.isEmpty(userPreference.getString("allSche", ""))) {
            allSche = GsonUtils.ScheGsonToBean(userPreference.getString("allSche", ""));
            for (ScheBean item : allSche) {
                LogUtil.d("Service", "item=" + item);
                if (item.getStartTime().split(" ")[0].equals(today)) {
                    LogUtil.d("Service", "今日日程=" + item.toString());
                    //设置日历的时间，主要是让日历的年月日和当前同步
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    //设置日历的小时和分钟
                    int h, s;
                    h = Integer.parseInt(item.getStartTime().split(" ")[2].split(":")[0]);
                    s = Integer.parseInt(item.getStartTime().split(" ")[2].split(":")[1]);

                    if (s - 10 < 0) {
                        s = s - 10 + 60;
                        h = h - 1;
                    } else {
                        s = s - 10;
                    }
                    LogUtil.d("Service", "h=" + h + ":" + s);
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, s);
                    //将秒和毫秒设置为0
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    LogUtil.d("Service", "闹钟时间=" + FormatUtils.getStrTime(calendar.getTime()));
                    //true发出提醒
                    Boolean a = getSharedPreferences("notice", Context.MODE_PRIVATE)
                            .getBoolean("notice", true);
                    Log.e("aa",a + "");
                    if (getSharedPreferences("notice", Context.MODE_PRIVATE)
                            .getBoolean("notice", true)) {
                        //建立Intent和PendingIntent来调用闹钟管理器
                        Intent intent = new Intent(this, AlarmServer.class);
                        intent.putExtra("sche", item);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        //获取闹钟管理器
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        //设置闹钟
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != updateScheDataBroadcast)
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateScheDataBroadcast);
    }

    private class UpdateScheDataBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_SCHE:
                    getAlarm();
                    break;
            }
        }
    }
}
