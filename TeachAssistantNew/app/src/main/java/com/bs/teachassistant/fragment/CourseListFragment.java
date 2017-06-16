package com.bs.teachassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.AddRecordActivity;
import com.bs.teachassistant.activity.CourseAddActivity;
import com.bs.teachassistant.activity.LessonsActivity;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.entity.Course;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt on 2017/6/6.
 */
@ContentView(R.layout.fragment_course_list)
public class CourseListFragment extends BaseFragment {

    @ViewInject(R.id.image_info_close)
    ImageView back;

    @ViewInject(R.id.list_course)
    ListView listView;

    @ViewInject(R.id.course_line_add)
    LinearLayout linearLayout;

    private List<Course> courseList;

    private CommDataAdapter<Course> courseAdapter;

    public static CourseListFragment newInstance(boolean back, int type, String title) {
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("back", back);
        bundle.putInt("type", type);
        bundle.putString("title", title);
        courseListFragment.setArguments(bundle);
        return courseListFragment;
    }

    @Override
    public void initViews() {
        courseList = new ArrayList<>();
        courseAdapter = new CommDataAdapter<Course>(courseList, R.layout.view_item_course) {
            @Override
            public void bindView(ViewHolder holder, Course obj) {
                holder.setText(R.id.txt_item_course_name, obj.getName());
                holder.setText(R.id.txt_item_course_time, obj.getTerm());
                holder.setText(R.id.txt_item_course_address, obj.getAddress());
            }
        };
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //根据type来跳转不同的Activity,0:备课，2：授课总结和教学日志
                Intent intent = new Intent();
                if(getArguments() == null) {
                    intent.setClass(getActivity(), LessonsActivity.class);
                    intent.putExtra("courseId", courseList.get(i).getId());
                    startActivity(intent);
                } else if(getArguments().getInt("type", 0) == 0) {
                    intent.setClass(getActivity(), LessonsActivity.class);
                    intent.putExtra("courseId", courseList.get(i).getId());
                    startActivity(intent);
                } else if(getArguments().getInt("type", 0) == 2){
                    intent.putExtra("title", getArguments().getString("title"));
                    intent.setClass(getActivity(), AddRecordActivity.class);
                    intent.putExtra("courseId", courseList.get(i).getId());
                    startActivityForResult(intent, 5);
                }


                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CourseAddActivity.class);
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

            }
        });

        if(getArguments() != null && getArguments().getBoolean("back", false)) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

    }

    @Override
    public void initDatas() {
        courseList = courseDao.loadAll();
        if(courseList != null && courseList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            courseAdapter.setmData(courseList);
        } else {
            listView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 3 && requestCode == 5) {
            //授课、教学日志保存后回退到上个界面
            getActivity().finish();
        }
        initDatas();
    }

    public void updateData() {
        initDatas();
    }

}
