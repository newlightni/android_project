package com.bs.teachassistant.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.StudBean;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/4/27.
 */
@ContentView(R.layout.dialog_activity_stud_info)
public class DialogInfoActivity extends BaseActivity {
    @ViewInject(R.id.edit_id)
    EditText editId;
    @ViewInject(R.id.edit_name)
    EditText editName;

    private String TAG = "DialogInfoActivity";
    private List<StudBean> allStudents;

    @Override
    public void initViews() {
        StudBean studBean = getIntent().getParcelableExtra("student");
        if (null != studBean) {
            editId.setText(studBean.getStuId());
            editName.setText(studBean.getName());
        }

        allStudents = new ArrayList<>();
        if (!TextUtils.isEmpty(userPreference.getString("students", ""))) {
            allStudents.clear();
            allStudents = GsonUtils.StudGsonToBean(userPreference.getString("students", ""));
        }
    }

    @Override
    public void initDatas() {

    }

    @Event(value = {R.id.btn_stud_repeat, R.id.btn_stud_yes}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_stud_repeat:
                editId.setText("");
                editName.setText("");
                break;
            case R.id.btn_stud_yes:
                addStudent();
                break;

        }
    }

    /**
     * 添加学生信息
     */
    private void addStudent() {
        if (TextUtils.isEmpty(editId.getText().toString())) {
            Toast.makeText(this, "学号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editName.getText().toString())) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (StudBean item : allStudents) {
            if (item.getStuId().equals(editId.getText().toString().replace(" ", ""))) {
                Toast.makeText(this, "已存在该学号", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        LogUtil.d(TAG, "" + editName.getText().toString() + "  " + editId.getText().toString());
        Intent intent = new Intent();
        intent.setAction(Comm.ACTION_UPDATE_ADD_STUD);
        intent.putExtra("student", new StudBean(editName.getText().toString(), editId.getText().toString(), "","", "", ""
            ,"",""));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        finish();
    }
}
