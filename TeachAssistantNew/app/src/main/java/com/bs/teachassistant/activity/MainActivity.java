package com.bs.teachassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.adapter.BaseFragmentAdapter;
import com.bs.teachassistant.fragment.BaseFragment;
import com.bs.teachassistant.fragment.CourseListFragment;
import com.bs.teachassistant.fragment.LessonFragment;
import com.bs.teachassistant.fragment.MineFragment;
import com.bs.teachassistant.fragment.ScheFragment;
import com.bs.teachassistant.fragment.SignFragment;
import com.bs.teachassistant.service.RemindService;
import com.bs.teachassistant.view.CustomViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.radio_group_main)
    RadioGroup radioMain;
    @ViewInject(R.id.vp_main)
    CustomViewPager viewPagerMain;
    @ViewInject(R.id.txt_add_note)
    TextView txtAdd;

    private BaseFragmentAdapter adapter;
    private PopupWindow popupWindow;

    private CourseListFragment courseListFragment;

    @Override
    public void initViews() {
        List<BaseFragment> fragments = new ArrayList<>();
        courseListFragment = new CourseListFragment();
        fragments.add(new ScheFragment());
        fragments.add(new SignFragment());
        //fragments.add(new LessonFragment());
        fragments.add(courseListFragment);
        fragments.add(new MineFragment());

        if (null == adapter)
            adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPagerMain.setAdapter(adapter);
        viewPagerMain.setCurrentItem(0);
        viewPagerMain.setOffscreenPageLimit(3);
        radioMain.setOnCheckedChangeListener(this);
    }

    @Override
    public void initDatas() {
        //初始默认提醒
        if(getSharedPreferences("notice",
                Context.MODE_PRIVATE) == null) {
            getSharedPreferences("notice", MODE_PRIVATE)
                    .edit().putBoolean("notice", true).apply();
        }
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopWindow(view);
                Intent intent = new Intent(MainActivity.this,
                        CheckClassActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.radio_1:
                viewPagerMain.setCurrentItem(0);
                break;
            case R.id.radio_2:
                viewPagerMain.setCurrentItem(1);
                break;
            case R.id.radio_4:
                viewPagerMain.setCurrentItem(2);
                courseListFragment.updateData();
                break;
            case R.id.radio_5:
                viewPagerMain.setCurrentItem(3);
                break;
        }
    }

    private void showPopWindow(final View parent) {
        View view = null;
        if (null == popupWindow) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.view_popwindow, null);
            TextView txt1 = (TextView) view.findViewById(R.id.txt_1);
            txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    //intent.setClass(MainActivity.this, AddRecordActivity.class);
                    intent.setClass(MainActivity.this, CourseListActivity.class);
                    intent.putExtra("title", "教学日志");
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
            });
            TextView txt2 = (TextView) view.findViewById(R.id.txt_2);
            txt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    //intent.setClass(MainActivity.this, AddRecordActivity.class);
                    intent.setClass(MainActivity.this, CourseListActivity.class);
                    intent.putExtra("title", "授课总结");
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
            });
            TextView txt3 = (TextView) view.findViewById(R.id.txt_3);
            txt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AddRecordActivity.class);
                    intent.putExtra("title", "便签记录");
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
            });
            TextView txt4 = (TextView) view.findViewById(R.id.txt_4);
            txt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("type", "update");
                    intent3.putExtra("title", "平时成绩");
                    intent3.setClass(MainActivity.this, AddScoreActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
                }
            });

            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
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
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, parent.getHeight() + 10);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }
}
