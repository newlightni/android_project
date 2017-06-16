package com.bs.teachassistant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.StudBean;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;
import com.bs.teachassistant.view.wheelview.NumericWheelAdapter;
import com.bs.teachassistant.view.wheelview.OnWheelChangedListener;
import com.bs.teachassistant.view.wheelview.WheelView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by limh on 2017/4/27.
 * 学生信息
 */
@ContentView(R.layout.activity_add_stud)
public class AddStudActivity extends BaseActivity {
    @ViewInject(R.id.list_add_stud)
    ListView listAddStud;
    @ViewInject(R.id.txt_stu_title)
    TextView txtTitle;
    @ViewInject(R.id.txt_add)
    TextView txtAdd;
    @ViewInject(R.id.txt_time)
    TextView txtTime;
    @ViewInject(R.id.txt_more)
    TextView txtMore;

    private List<StudBean> allStuds;
    private CommDataAdapter<StudBean> adapter;
    private AddStudentBroadcast studentBroadcast;
    private static String TAG = "AddStudActivity";
    private String type = "add";

    @Override
    public void initViews() {
        //注册广播 接收对话框来的数据
        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_STUD);
        studentBroadcast = new AddStudentBroadcast();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(studentBroadcast, filter);
        allStuds = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        switch (type) {
            case "check":
                txtAdd.setVisibility(View.GONE);
                txtTime.setVisibility(View.VISIBLE);
                txtTitle.setText("签到情况");
                txtMore.setText("签到状态");
                allStuds.clear();
                allStuds = GsonUtils.StudGsonToBean(userPreference.getString("signs", ""));
                break;

            case "add":
                if (!TextUtils.isEmpty(userPreference.getString("students", ""))) {
                    allStuds.clear();
                    allStuds = GsonUtils.StudGsonToBean(userPreference.getString("students", ""));
                }
                break;
        }

        //list长按事件
        listAddStud.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (type.equals("add")) {
                    Intent intent = new Intent(AddStudActivity.this, DialogInfoActivity.class);
                    intent.putExtra("student", allStuds.get(i));
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    public void initDatas() {
        updateList();
    }

    private void updateList() {
        if (null == adapter) {
            adapter = new CommDataAdapter<StudBean>(allStuds, R.layout.view_item_stud) {
                @Override
                public void bindView(ViewHolder holder, StudBean obj) {
                    holder.setText(R.id.txt_id, "" + (allStuds.indexOf(obj) + 1));
                    holder.setText(R.id.txt_stud_id, obj.getStuId());
                    holder.setText(R.id.txt_stud_name, obj.getName());
                    holder.setText(R.id.txt_stud_remark, obj.getStatus());
                    holder.setText(R.id.txt_stud_time, obj.getSignTime());
                    holder.setText(R.id.txt_stud_class, obj.getGroup());
                    holder.setText(R.id.txt_stud_term, obj.getTerm());
                    holder.setText(R.id.txt_stud_course, obj.getCourse());
                    if (type.equals("check")) {
                        holder.setVisibility(R.id.txt_stud_time, View.VISIBLE);
                    }
                }
            };
            listAddStud.setAdapter(adapter);
        } else {
            adapter.setmData(allStuds);
        }
    }

    @Event(value = {R.id.txt_add, R.id.image_info_close}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_add:
                Intent intent = new Intent();
                intent.setClass(AddStudActivity.this, DialogInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.image_info_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;

        }
    }

    private class AddStudentBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_STUD:
                    StudBean stud = intent.getParcelableExtra("student");
                    if (null != stud) {
                        LogUtil.d(TAG, "STUDENT=" + stud.toString());
                        allStuds.add(stud);
                        userPreference.edit().putString("students", GsonUtils.GsonString(allStuds)).apply();
                        updateList();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null == studentBroadcast)
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(studentBroadcast);
    }
}
