package com.bs.teachassistant.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.entity.Course;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_course)
public class CourseActivity extends BaseActivity {

    @ViewInject(R.id.list_course)
    ListView listNote;

    @ViewInject(R.id.txt_course_add)
    ImageView courseAdd;

    @ViewInject(R.id.course_line_add)
    LinearLayout lineAdd;

    private List<Course> courseList;

    private CommDataAdapter<Course> courseAdapter;

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
        listNote.setAdapter(courseAdapter);
        courseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CourseActivity.this, CourseAddActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

            }
        });

        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(CourseActivity.this, CourseAddActivity.class);
                intent.putExtra("course", courseList.get(i).getId());
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });
    }

    @Override
    public void initDatas() {
        //此处最好加线程，模拟加载
        courseList = courseDao.loadAll();
        if(courseList == null || courseList.size() == 0) {
            listNote.setVisibility(View.GONE);
            lineAdd.setVisibility(View.VISIBLE);
            lineAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(CourseActivity.this, CourseAddActivity.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
            });
        } else {
            listNote.setVisibility(View.VISIBLE);
            lineAdd.setVisibility(View.GONE);
            courseAdapter.setmData(courseList);
            //courseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initDatas();
    }
}
