package com.bs.teachassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.bs.teachassistant.database.ScoreDao;
import com.bs.teachassistant.entity.Score;
import com.bs.teachassistant.utils.LogUtil;
import com.bs.teachassistant.view.wheelview.NumericWheelAdapter;
import com.bs.teachassistant.view.wheelview.OnWheelChangedListener;
import com.bs.teachassistant.view.wheelview.WheelView;

import org.greenrobot.greendao.query.QueryBuilder;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
@ContentView(R.layout.activity_add_score)
public class AddScoreActivity extends BaseActivity {
    @ViewInject(R.id.txt_term)
    TextView txtTerm;
    @ViewInject(R.id.edit_class)
    EditText editClass;
    @ViewInject(R.id.list_add_score)
    ListView listScore;

    private PopupWindow popupWindow;
    private List<String> datas;
    private String grade = "大一 第1学期";
    private Score score;

    private List<Score> currScores;
    private CommDataAdapter<Score> adapter;

    @Override
    public void initViews() {
        datas = new ArrayList<>();
        datas.clear();
        datas.addAll(Arrays.asList(getResources().getStringArray(R.array.array_grade)));
        currScores = new ArrayList<>();
        listScore.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                score = currScores.get(position);
                showPopWindow(view);
                return false;
            }
        });
    }

    @Override
    public void initDatas() {
        QueryBuilder qb = scoreDao.queryBuilder();
        qb.where(ScoreDao.Properties.Term.eq(grade));
        List list = qb.list();
        currScores.clear();
        for (Object item : list) {
            currScores.add((Score) item);
            LogUtil.d("addScore",item.toString());
        }
        updateList();
    }

    private void updateList() {
        if (null == adapter) {
            adapter = new CommDataAdapter<Score>(currScores, R.layout.view_item_score) {
                @Override
                public void bindView(ViewHolder holder, Score obj) {
                    holder.setText(R.id.txt_stu_id, obj.getStuId());
                    holder.setText(R.id.txt_stu_name, obj.getName());
                    holder.setText(R.id.txt_user_name, obj.getName());
                    holder.setText(R.id.txt_score, "" + obj.getScore());
                    holder.setText(R.id.txt_course, obj.getClassName());
                }
            };
            listScore.setAdapter(adapter);
        } else {
            adapter.setmData(currScores);
        }
    }

    @Event(value = {R.id.image_add, R.id.txt_term, R.id.image_info_close}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_add:
                score = null;
                showPopWindow(view);
                break;
            case R.id.txt_term:
                showPopTerm(view);
                break;
            case R.id.image_info_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
        }
    }

    private void showPopWindow(final View parent) {
        View view;
        final EditText edit;
        final EditText edit1;
        final EditText edit2;
        final EditText edit3;
        final EditText edit4;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.view_popwindow_socre, null);
        edit = (EditText) view.findViewById(R.id.edit_socre_id);
        edit1 = (EditText) view.findViewById(R.id.edit_socre_class);
        edit2 = (EditText) view.findViewById(R.id.edit_socre_name);
        edit3 = (EditText) view.findViewById(R.id.edit_class_name);
        edit4 = (EditText) view.findViewById(R.id.edit_group_name);
        TextView txtCancel = (TextView) view.findViewById(R.id.btn_socre_cancel);

        if (null != score) {
            edit.setText(String.valueOf(score.getScore()));
            edit1.setText(score.getStuId());
            edit2.setText(score.getName());
            edit3.setText(score.getClassName());
            edit4.setText(score.getGroupName());
            txtCancel.setText("删除");
            LogUtil.d("addScore",score.toString());
        }

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=score){
                    scoreDao.delete(score);
                    initDatas();
                }
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });

        TextView txtYes = (TextView) view.findViewById(R.id.btn_socre_yes);
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Score score1 = new Score();
                score1.setTerm(grade);
                score1.setStuId(edit1.getText().toString());
                score1.setName(edit2.getText().toString());
                score1.setClassName(edit3.getText().toString());
                score1.setGroupName(edit4.getText().toString());
                score1.setScore(Integer.parseInt(edit.getText().toString()));
                if (score == null)
                    scoreDao.insert(score1);
                else {
                    LogUtil.d("addScore","更新数据="+score);
                    score1.setId(score.getId());
                    scoreDao.update(score1);
                }
                initDatas();
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, parent.getHeight() + 10);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }

    private void showPopTerm(View parent) {
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
                initDatas();
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
                WindowManager.LayoutParams lp = AddScoreActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                AddScoreActivity.this.getWindow().setAttributes(lp);
            }
        });
    }
}
