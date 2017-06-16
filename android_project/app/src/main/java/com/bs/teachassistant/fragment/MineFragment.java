package com.bs.teachassistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.AddLessActivity;
import com.bs.teachassistant.activity.CheckClassActivity;
import com.bs.teachassistant.activity.CourseActivity;
import com.bs.teachassistant.activity.LoginActivity;
import com.bs.teachassistant.activity.ShareActivity;
import com.bs.teachassistant.database.CourseDao;
import com.bs.teachassistant.database.LessDao;
import com.bs.teachassistant.database.NoteDao;
import com.bs.teachassistant.database.ScoreDao;
import com.bs.teachassistant.entity.User;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.view.CircleTransform;
import com.bs.teachassistant.view.MessageDialog;
import com.bs.teachassistant.view.SwitchButton;
import com.bs.teachassistant.view.wheelview.NumericWheelAdapter;
import com.bs.teachassistant.view.wheelview.OnWheelChangedListener;
import com.bs.teachassistant.view.wheelview.WheelView;
import com.squareup.picasso.Picasso;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by limh on 2017/4/24.
 * 设置页面
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment implements SwitchButton.OnSwitchChangeListener {
    @ViewInject(R.id.image_user_header)
    ImageView imageHeader;
    @ViewInject(R.id.txt_user_name)
    TextView txtName;
    @ViewInject(R.id.text_notice_switch)
    SwitchButton switchButton;
    User user;

    private MessageDialog dialog;

    private List<String> datas;
    private PopupWindow popupWindow;
    private String grade = "大一 第1学期";


    @Override
    public void initViews() {
        if (!TextUtils.isEmpty(userPreference.getString("useInfo", ""))) {
            user = GsonUtils.GsonToBean(userPreference.getString("useInfo", ""), User.class);
            if (user.getSex().equals("boy")) {
                Picasso.with(getContext()).load(R.drawable.default_boy).
                        transform(new CircleTransform()).into(imageHeader);
            } else {
                Picasso.with(getContext()).load(R.drawable.default_girl).
                        transform(new CircleTransform()).into(imageHeader);
            }
            txtName.setText(user.getUserName());
        } else {
            Picasso.with(getContext()).load(R.drawable.default_boy).
                    transform(new CircleTransform()).into(imageHeader);
        }
        switchButton.setSwitchStatus(getActivity().getSharedPreferences("notice",
                Context.MODE_PRIVATE).getBoolean("notice", true));
        switchButton.setOnSwitchChangeListener(this);
    }

    @Override
    public void initDatas() {
        datas=new ArrayList<>();
        datas.clear();
        datas.addAll(Arrays.asList(getResources().getStringArray(R.array.array_grade)));
    }

    @Event(value = {R.id.txt_user_name, R.id.txt_collec,R.id.txt_record,R.id.txt_course,
            R.id.txt_clear,
            R.id.txt_about, R.id.txt_exit, R.id.txt_tucao}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_user_name:
                if (null != user) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
                break;
            case R.id.txt_collec:
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShareActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.txt_course:
                Intent intentCourse = new Intent();
                intentCourse.setClass(getActivity(), CourseActivity.class);
                startActivity(intentCourse);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.txt_record:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), CheckClassActivity.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;

            case R.id.txt_tucao:
                showMsgDialog("提示", "请上线后再来吐槽");
                break;
            case R.id.txt_about:
                showMsgDialog("关于App", "高校教师助手" + " V" + FormatUtils.getVersionName(getContext()) + "\n"
                        + "石家庄铁道大学：孟晓明毕业设计");
                break;
            case R.id.txt_exit:
                showExitDialog();
                break;

            //清除数据
            case R.id.txt_clear:
                showPopWindow(view);
                break;
        }
    }

    /**
     * 展示信息提示对话框
     */
    private void showExitDialog() {
        if (null == dialog)
            dialog = MessageDialog.getInstence(getActivity());
        dialog.setCustomDialog("提示", "您确定要退出吗？", "取消", "确定");
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent intent=new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        if (!dialog.isShowing())
            dialog.show();
    }

    /**
     * 退出提示对话框
     *
     * @param title 对话框标题
     * @param msg   对话框信息内容
     */
    private void showMsgDialog(String title, String msg) {
        if (null == dialog)
            dialog = MessageDialog.getInstence(getActivity());
        dialog.setCustomDialog(title, msg, "", "确定");
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        if (!dialog.isShowing())
            dialog.show();
    }

    @Override
    public void onSwitchChanged(boolean open) {
        //保存是否提醒
       getActivity().getSharedPreferences("notice", Context.MODE_PRIVATE)
               .edit().putBoolean("notice", open).apply();
    }

    //清除数据
    private void showPopWindow(View parent) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_item_wheel, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_wheel_title);
        WheelView wvItem = (WheelView) view.findViewById(R.id.wv_item);
        wvItem.setAdapter(new NumericWheelAdapter(datas));
        wvItem.setCyclic(false);
        wvItem.setCurrentItem(datas.indexOf("大一 第1学期"));
        txtTitle.setText("选择学期");
        wvItem.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(View context, int oldValue, int newValue) {
                grade = datas.get(newValue);
            }
        });
        TextView txtCancel = (TextView) view.findViewById(R.id.btn_1);
        txtCancel.setText("全部清除");
        TextView txtYes = (TextView) view.findViewById(R.id.btn_2);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                clearCourse(null);
            }
        });
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popupWindow.isShowing())
                    popupWindow.dismiss();
                clearCourse(grade);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void clearCourse(String gradle) {
        if(gradle == null) {
            courseDao.deleteAll();
            lessDao.deleteAll();
            noteDao.deleteAll();
            scoreDao.deleteAll();
        } else {
            QueryBuilder qb = courseDao.queryBuilder();
            qb.where(CourseDao.Properties.Term.eq(gradle));
            qb.buildDelete();
            QueryBuilder qb1 = noteDao.queryBuilder();
            qb1.where(NoteDao.Properties.Term.eq(gradle));
            qb1.buildDelete();
            QueryBuilder qb2 = lessDao.queryBuilder();
            qb2.where(LessDao.Properties.Term.eq(gradle));
            qb2.buildDelete();
            QueryBuilder qb3 = scoreDao.queryBuilder();
            qb3.where(ScoreDao.Properties.Term.eq(gradle));
            qb3.buildDelete();
        }
    }

}
