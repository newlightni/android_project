package com.bs.teachassistant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.view.SpecialCalendar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by limh on 2017/4/24.
 * 添加日程
 */
@ContentView(R.layout.activity_add_sche)
public class AddScheActivity extends BaseActivity {
    @ViewInject(R.id.edit_title)
    EditText editTitle;
    @ViewInject(R.id.edit_loacl)
    EditText editLocal;
    @ViewInject(R.id.edit_remark)
    EditText editRemark;
    @ViewInject(R.id.txt_start_time)
    TextView txtStart;
    @ViewInject(R.id.txt_end_time)
    TextView txtEnd;

    public int seetingFlag = -1;
    private UpdateTimeBroadcast updateTimeBroadcast;
    private List<ScheBean> scheDatas;

    @Override
    public void initViews() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        txtStart.setText(FormatUtils.getDate() + " " + FormatUtils.formatWeek(SpecialCalendar.getWeekdayOfMonth(year, month, day)) + " 08:00");
        txtEnd.setText(FormatUtils.getDate() + " " + FormatUtils.formatWeek(SpecialCalendar.getWeekdayOfMonth(year, month, day)) + " 08:00");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_TIME);
        updateTimeBroadcast = new UpdateTimeBroadcast();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateTimeBroadcast, filter);
    }

    @Override
    public void initDatas() {
        scheDatas = new ArrayList<>();
        if (!TextUtils.isEmpty(userPreference.getString("allSche", ""))) {
            scheDatas = GsonUtils.ScheGsonToBean(userPreference.getString("allSche", ""));
        }
    }

    @Event(value = {R.id.txt_sche_close, R.id.txt_sche_yes, R.id.txt_start_time,
            R.id.txt_end_time}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_start_time:
                seetingFlag = 0;
                Intent intent = new Intent(AddScheActivity.this, DateTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_end_time:
                seetingFlag = 1;
                Intent intent1 = new Intent(AddScheActivity.this, DateTimeActivity.class);
                startActivity(intent1);
                break;
            case R.id.txt_sche_close:
                finish();
                break;
            case R.id.txt_sche_yes:
                saveSche();
                break;
        }
    }

    private void saveSche() {
        if (TextUtils.isEmpty(editTitle.getText().toString())) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editLocal.getText().toString())) {
            Toast.makeText(this, "活动地点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        scheDatas.add(new ScheBean(editTitle.getText().toString(), editLocal.getText().toString(),
                txtStart.getText().toString(), txtEnd.getText().toString(), editRemark.getText().toString(), false));
        //保存所有日程数据
        userPreference.edit().putString("allSche", GsonUtils.GsonString(scheDatas)).apply();
        Intent intent = new Intent();
        intent.setAction(Comm.ACTION_UPDATE_ADD_SCHE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private class UpdateTimeBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_TIME:
                    switch (seetingFlag) {
                        case 0:
                            txtStart.setText(intent.getStringExtra("year") + "/" + intent.getStringExtra("month") + "/" +
                                    intent.getStringExtra("day") + " " + FormatUtils.formatWeek(intent.getIntExtra("week", 0) - 1) + " " +
                                    intent.getStringExtra("hour") + ":" + intent.getStringExtra("minute"));
                            break;
                        case 1:
                            txtEnd.setText(intent.getStringExtra("year") + "/" + intent.getStringExtra("month") + "/" +
                                    intent.getStringExtra("day") + " " + FormatUtils.formatWeek(intent.getIntExtra("week", 0) - 1) + " " +
                                    intent.getStringExtra("hour") + ":" + intent.getStringExtra("minute"));
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null == updateTimeBroadcast)
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateTimeBroadcast);
    }
}
