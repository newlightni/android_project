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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.Less;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;
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
 * 添加备课信息
 */
@ContentView(R.layout.activity_add_less)
public class AddLessActivity extends BaseActivity {
    @ViewInject(R.id.edit_less_name)
    EditText editName;
    @ViewInject(R.id.edit_less_chapter)
    EditText editChapter;
    @ViewInject(R.id.edit_less_content)
    EditText editContent;
    @ViewInject(R.id.txt_less_time)
    TextView txtLessTime;
    @ViewInject(R.id.txt_nav_title)
    TextView txtTitle;
    @ViewInject(R.id.line_edit)
    LinearLayout lineEdit;
    @ViewInject(R.id.txt_term)
    TextView txtTerm;
    @ViewInject(R.id.txt_less_pert)
    EditText editPert;
    @ViewInject(R.id.txt_local)
    EditText editLocal;

    private UpdateTimeBroadcast updateTimeBroadcast;
    private Less less;

    private List<String> datas;
    private String grade = "大一 第1学期";
    private PopupWindow popupWindow;

    @Override
    public void initViews() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_TIME);
        updateTimeBroadcast = new UpdateTimeBroadcast();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateTimeBroadcast, filter);
    }

    @Override
    public void initDatas() {

        datas=new ArrayList<>();
        datas.clear();
        datas.addAll(Arrays.asList(getResources().getStringArray(R.array.array_grade)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        less = getIntent().getParcelableExtra("less");
        if (null != less) {
            txtTitle.setText("课程记录");
            editName.setEnabled(false);
            editChapter.setEnabled(false);
            editContent.setEnabled(false);
            txtLessTime.setEnabled(false);
            txtTerm.setEnabled(false);
            editLocal.setEnabled(false);
            editPert.setEnabled(false);

            lineEdit.setVisibility(View.VISIBLE);
            editName.setText(less.getName());
            editChapter.setText(less.getChapter());
            editContent.setText(less.getContent());
            txtLessTime.setText(less.getTime());
            txtTerm.setText(less.getTerm());
            editLocal.setText(less.getLocal());
            editPert.setText(less.getPert());
        }
    }

    @Event(value = {R.id.txt_less_close, R.id.txt_less_yes, R.id.txt_term,
            R.id.txt_less_time, R.id.txt_edit, R.id.txt_delete}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_less_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.txt_less_yes:
                if (null == less){
                    addLess();
                }else {
                    less.setChapter(editChapter.getText().toString());
                    less.setName(editName.getText().toString());
                    less.setTime(txtLessTime.getText().toString());
                    less.setLocal(editLocal.getText().toString());
                    less.setTerm(txtTerm.getText().toString());
                    less.setContent(editContent.getText().toString());
                    less.setPert(editPert.getText().toString());
                    lessDao.update(less);
                    Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_less_time:
                Intent intent = new Intent(AddLessActivity.this, DateTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_edit:
                editName.setEnabled(true);
                editChapter.setEnabled(true);
                editContent.setEnabled(true);
                txtLessTime.setEnabled(true);
                txtTerm.setEnabled(true);
                editLocal.setEnabled(true);
                editPert.setEnabled(true);
                break;
            case R.id.txt_delete:
                lessDao.delete(less);
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                Intent intent2 = new Intent();
                intent2.setAction(Comm.ACTION_UPDATE_ADD_LESS);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);

                Intent intent1 = new Intent();
                intent1.setAction(Comm.ACTION_UPDATE_ADD_SCHE);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                break;
            case R.id.txt_term:
                showPopWindow(view);
                break;
        }
    }

    private void addLess() {
        Log.w("LESS", "ADDlESS");
        if (TextUtils.isEmpty(editName.getText().toString())) {
            Toast.makeText(this, "课程名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editChapter.getText().toString())) {
            Toast.makeText(this, "章节不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editContent.getText().toString())) {
            Toast.makeText(this, "备课内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Less less=new Less();
        less.setChapter(editChapter.getText().toString());
        less.setName(editName.getText().toString());
        less.setTime(txtLessTime.getText().toString());
        less.setLocal(editLocal.getText().toString());
        less.setTerm(txtTerm.getText().toString());
        less.setContent(editContent.getText().toString());
        less.setCourseId(getIntent().getLongExtra("courseId", 0));
        less.setPert(editPert.getText().toString());
        //数据添加到数据库
        lessDao.insert(less);

        List<ScheBean> scheDatas = new ArrayList<>();
        if (!TextUtils.isEmpty(userPreference.getString("allSche", ""))) {
            scheDatas = GsonUtils.ScheGsonToBean(userPreference.getString("allSche", ""));
        }
        //保存所有日程数据
        scheDatas.add(new ScheBean(editName.getText().toString(), "", txtLessTime.getText().toString(), txtLessTime.getText().toString(), editContent.getText().toString(), false));
        userPreference.edit().putString("allSche", GsonUtils.GsonString(scheDatas)).apply();

        Intent intent = new Intent();
        intent.setAction(Comm.ACTION_UPDATE_ADD_LESS);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        Intent intent1 = new Intent();
        intent1.setAction(Comm.ACTION_UPDATE_ADD_SCHE);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
        finish();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    }

    private class UpdateTimeBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_TIME:
                    txtLessTime.setText(intent.getStringExtra("year") + "/" + intent.getStringExtra("month") + "/" +
                            intent.getStringExtra("day") + " " + FormatUtils.formatWeek(intent.getIntExtra("week", 0) - 1) + " " +
                            intent.getStringExtra("hour") + ":" + intent.getStringExtra("minute"));
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

    private void showPopWindow(View parent) {
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_item_wheel, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_wheel_title);
        WheelView wvItem = (WheelView) view.findViewById(R.id.wv_item);
        wvItem.setAdapter(new NumericWheelAdapter(datas));
        wvItem.setCyclic(false);
        wvItem.setCurrentItem(datas.indexOf(grade));
        txtTitle.setText("选择学期");
        wvItem.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                grade = datas.get(newValue);
                txtTerm.setText(grade);
            }
        });
        TextView txtCancel = (TextView) view.findViewById(R.id.btn_1);
        TextView txtYes = (TextView) view.findViewById(R.id.btn_2);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.6f;
        this.getWindow().setAttributes(lp);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = AddLessActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                AddLessActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

}
