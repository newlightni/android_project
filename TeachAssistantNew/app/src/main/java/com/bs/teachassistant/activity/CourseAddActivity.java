package com.bs.teachassistant.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.entity.Course;
import com.bs.teachassistant.view.wheelview.NumericWheelAdapter;
import com.bs.teachassistant.view.wheelview.OnWheelChangedListener;
import com.bs.teachassistant.view.wheelview.WheelView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ContentView(R.layout.activity_course_add)
public class CourseAddActivity extends BaseActivity {

    @ViewInject(R.id.course_add_finish)
    TextView courseAddFinish;

    @ViewInject(R.id.course_add_name)
    EditText addName;

    @ViewInject(R.id.course_add_address)
    EditText addAddress;

    @ViewInject(R.id.course_add_term)
    TextView addTerm;

    @ViewInject(R.id.course_add_description)
    EditText addDescription;

    @ViewInject(R.id.line_edit)
    LinearLayout lineEdit;

    private PopupWindow popupWindow;

    private List<String> datas;
    private String grade = "大一 第1学期";

    private Course courseEdit;

    @Override
    public void initViews() {
        courseEdit = courseDao.load(getIntent().getLongExtra("course", 0));
        if(courseEdit != null) {
            addName.setText(courseEdit.getName());
            addAddress.setText(courseEdit.getAddress());
            addDescription.setText(courseEdit.getDescription());
            addTerm.setText(courseEdit.getTerm());
            addName.setEnabled(false);
            addAddress.setEnabled(false);
            addDescription.setEnabled(false);
            addTerm.setEnabled(false);
            lineEdit.setVisibility(View.VISIBLE);
        }
        courseAddFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(courseEdit != null) {
                    courseEdit.setName(addName.getText().toString());
                    courseEdit.setAddress(addAddress.getText().toString());
                    courseEdit.setDescription(addDescription.getText().toString());
                    courseEdit.setTerm(addTerm.getText().toString());
                    courseDao.update(courseEdit);
                } else {
                    Course course = new Course();
                    course.setName(addName.getText().toString());
                    course.setAddress(addAddress.getText().toString());
                    course.setDescription(addDescription.getText().toString());
                    course.setTerm(addTerm.getText().toString());
                    courseDao.insert(course);
                }
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });

        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow(view);
            }
        });
        addTerm.setText(grade);
    }

    @Override
    public void initDatas() {
        datas=new ArrayList<>();
        datas.clear();
        datas.addAll(Arrays.asList(getResources().getStringArray(R.array.array_grade)));
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
                addTerm.setText(grade);
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
                WindowManager.LayoutParams lp = CourseAddActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                CourseAddActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    @Event(value = {R.id.txt_edit, R.id.txt_delete}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {

            case R.id.txt_edit:
                addName.setEnabled(true);
                addTerm.setEnabled(true);
                addDescription.setEnabled(true);
                addAddress.setEnabled(true);
                break;
            case R.id.txt_delete:
                courseDao.delete(courseEdit);
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
        }
    }




}
