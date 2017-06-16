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
@ContentView(R.layout.activity_dialog_year_time)
public class YearActivity extends BaseActivity {
    @ViewInject(R.id.wv_year)
    WheelView wvYear;


    private String currentYear;

    List<String> yearDatas;

    private static String TAG="DateTimeActivity";
    @Override
    public void initViews() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        yearDatas = new ArrayList<>();
        for (int i = -50; i < 50; i++) {
            yearDatas.add("" + (2017 + i));
        }
        wvYear.setAdapter(new NumericWheelAdapter(yearDatas));
        wvYear.setCurrentItem(50);
        currentYear = yearDatas.get(50);
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
    }

    @Event(value = {R.id.txt_dialog_cancel, R.id.txt_dialog_yes}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_dialog_cancel:
                finish();
                break;
            case R.id.txt_dialog_yes:
                Intent intent = new Intent();
                intent.putExtra("year", currentYear);
                setResult(1, intent);
                finish();
                break;
        }
    }
}
