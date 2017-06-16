package com.bs.teachassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.bs.teachassistant.entity.Note;
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
 * Created by limh on 2017/5/2.
 * 添加日志
 */
@ContentView(R.layout.activity_add_record)
public class AddRecordActivity extends BaseActivity {
    @ViewInject(R.id.txt_record_title)
    TextView txtTitle;
    @ViewInject(R.id.txt_record_time)
    TextView txtTime;
    @ViewInject(R.id.image_record_finish)
    TextView txtFinish;
    @ViewInject(R.id.txt_note_term)
    TextView txtTerm;
    @ViewInject(R.id.txt_note_pert)
    EditText editPert;
    @ViewInject(R.id.edit_note_1)
    EditText edit1;
    @ViewInject(R.id.edit_note_local)
    EditText editLocal;
    @ViewInject(R.id.edit_note_2)
    EditText edit2;
    @ViewInject(R.id.edit_note_3)
    EditText edit3;
    @ViewInject(R.id.edit_id_note_content)
    EditText editContent;
    @ViewInject(R.id.line_edit)
    LinearLayout lineEdit;
    @ViewInject(R.id.line_1)
    LinearLayout line1;
    @ViewInject(R.id.line_2)
    LinearLayout line2;


    private Note note;
    private List<String> datas;
    private String grade = "大一 第1学期";
    private PopupWindow popupWindow;

    @Override
    public void initViews() {
        datas = new ArrayList<>();
        datas.clear();
        datas.addAll(Arrays.asList(getResources().getStringArray(R.array.array_grade)));
        note = getIntent().getParcelableExtra("note");
        if (null != note) {
            txtTitle.setText(note.getCalssify());
            txtTime.setText(note.getTime());
            edit1.setText(note.getCalssName());
            edit2.setText(note.getTitle());
            edit3.setText(note.getRemark());
            editContent.setText(note.getContent());
            editPert.setText(note.getPert());

            edit1.setEnabled(false);
            edit2.setEnabled(false);
            edit3.setEnabled(false);
            editContent.setEnabled(false);
            editPert.setEnabled(false);
            txtFinish.setVisibility(View.INVISIBLE);
            lineEdit.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText(getIntent().getStringExtra("title"));
            txtTime.setText(FormatUtils.getDateTime());
        }
        switch (txtTitle.getText().toString()) {
            case "教学日志":
                edit1.setHint("课程名称");
                edit2.setHint("课程章节");
                edit3.setHint("小标题/备注");
                break;
            case "授课总结":
                edit1.setHint("课程名称");
                edit2.setHint("课程章节");
                edit3.setVisibility(View.GONE);
                break;
            default:
                edit1.setHint("标题");
                edit2.setHint("备注");
                edit3.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void initDatas() {
    }

    @Event(value = {R.id.image_record_close, R.id.image_record_finish, R.id.txt_note_term,
            R.id.txt_edit, R.id.txt_delete}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_record_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.image_record_finish:
                if (null == note){
                    saveNote();
                }else {
                    note.setCalssify(txtTitle.getText().toString());
                    note.setTime(txtTime.getText().toString());
                    note.setLocal(editLocal.getText().toString());
                    note.setCalssName(edit1.getText().toString());
                    note.setTitle(edit2.getText().toString());
                    note.setRemark(edit3.getText().toString());
                    note.setContent(editContent.getText().toString());
                    note.setTerm(txtTerm.getText().toString());
                    note.setPert(editPert.getText().toString());
                    noteDao.update(note);
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_edit:
                edit1.setEnabled(true);
                edit2.setEnabled(true);
                edit3.setEnabled(true);
                editContent.setEnabled(true);
                editPert.setEnabled(true);
                txtFinish.setVisibility(View.VISIBLE);

                break;
            case R.id.txt_delete:
                noteDao.delete(note);
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.txt_note_term:
                showPopWindow(view);
                break;
        }
    }

    private void saveNote() {
        if (TextUtils.isEmpty(editContent.getText().toString())) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note();
        note.setCalssify(txtTitle.getText().toString());
        note.setTime(txtTime.getText().toString());
        note.setLocal(editLocal.getText().toString());
        note.setCalssName(edit1.getText().toString());
        note.setTitle(edit2.getText().toString());
        note.setRemark(edit3.getText().toString());
        note.setContent(editContent.getText().toString());
        note.setTerm(txtTerm.getText().toString());
        note.setPert(editPert.getText().toString());
        note.setCourseId(getIntent().getLongExtra("courseId", 0));
        noteDao.insert(note);
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
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
                WindowManager.LayoutParams lp = AddRecordActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                AddRecordActivity.this.getWindow().setAttributes(lp);
            }
        });
    }
}
