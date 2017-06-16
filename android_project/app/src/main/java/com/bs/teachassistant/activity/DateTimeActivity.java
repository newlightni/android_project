package com.bs.teachassistant.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.LogUtil;
import com.bs.teachassistant.view.SpecialCalendar;
import com.bs.teachassistant.view.wheelview.NumericWheelAdapter;
import com.bs.teachassistant.view.wheelview.OnWheelChangedListener;
import com.bs.teachassistant.view.wheelview.WheelView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by limh on 2017/4/25.
 * 日期时间对话框
 */
@ContentView(R.layout.activity_dialog_date_time)
public class DateTimeActivity extends BaseActivity {
    @ViewInject(R.id.wv_year)
    WheelView wvYear;
    @ViewInject(R.id.wv_month)
    WheelView wvMonth;
    @ViewInject(R.id.wv_day)
    WheelView wvDay;
    @ViewInject(R.id.wv_hour)
    WheelView wvHour;
    @ViewInject(R.id.wv_minute)
    WheelView wvMinute;

    private String currentYear, currentMonth, currentDay, currentHour, currentMinute;

    List<String> yearDatas;
    List<String> monthatas;
    List<String> dayDatas;
    List<String> hourDatas;
    List<String> minuteDatas;

    private static String TAG="DateTimeActivity";
    @Override
    public void initViews() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);
        yearDatas = new ArrayList<>();
        for (int i = -50; i < 50; i++) {
            yearDatas.add("" + (2017 + i));
        }
        wvYear.setAdapter(new NumericWheelAdapter(yearDatas));
        wvYear.setCurrentItem(50);

        monthatas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            monthatas.add("" + FormatUtils.formatSuff(i + 1));
        }
        wvMonth.setAdapter(new NumericWheelAdapter(monthatas));
        wvMonth.setCurrentItem(month-1);

        dayDatas = new ArrayList<>();
        for (int i = 0; i < SpecialCalendar.getDaysOfMonth(SpecialCalendar.isLeapYear(year), month); i++) {
            dayDatas.add("" + FormatUtils.formatSuff(i + 1));
        }
        wvDay.setAdapter(new NumericWheelAdapter(dayDatas));
        wvDay.setCurrentItem(day-1);

        hourDatas = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourDatas.add(FormatUtils.formatSuff(i));
        }
        wvHour.setAdapter(new NumericWheelAdapter(hourDatas));
        wvHour.setCurrentItem(8);

        minuteDatas = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minuteDatas.add(FormatUtils.formatSuff(i));
        }
        wvMinute.setAdapter(new NumericWheelAdapter(minuteDatas));
        wvMinute.setCurrentItem(0);

        currentYear = "" + year;
        currentMonth = FormatUtils.formatSuff(month);
        currentDay = FormatUtils.formatSuff(day);
        currentHour = "08";
        currentMinute = "00";
    }

    @Override
    public void initDatas() {
        wvYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                currentYear = yearDatas.get(newValue);
                LogUtil.d(TAG,currentYear);
            }
        });
        wvMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                currentMonth = monthatas.get(newValue);
                LogUtil.d(TAG,currentMonth);
            }
        });
        wvDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                currentDay = dayDatas.get(newValue);
                LogUtil.d(TAG,currentDay);
            }
        });
        wvHour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                currentHour = hourDatas.get(newValue);
                LogUtil.d(TAG,currentHour);
            }
        });
        wvMinute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                currentMinute = minuteDatas.get(newValue);
                LogUtil.d(TAG,currentMinute);
            }
        });
    }

    @Event(value = {R.id.txt_dialog_cancel, R.id.txt_dialog_yes}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_dialog_cancel:
                finish();
                break;
            case R.id.txt_dialog_yes:
                Intent intent = new Intent();
                intent.setAction(Comm.ACTION_UPDATE_ADD_TIME);
                intent.putExtra("year", currentYear);
                intent.putExtra("month", currentMonth);
                intent.putExtra("day", currentDay);
                intent.putExtra("hour", currentHour);
                intent.putExtra("minute", currentMinute);
                intent.putExtra("week",SpecialCalendar.getWeekdayOfMonth(Integer.parseInt(currentYear),Integer.parseInt(currentMonth),Integer.parseInt(currentDay)));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                finish();
                break;
        }
    }
}
