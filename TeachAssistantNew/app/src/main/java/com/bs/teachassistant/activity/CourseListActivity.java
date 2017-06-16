package com.bs.teachassistant.activity;

import com.bs.teachassistant.R;
import com.bs.teachassistant.fragment.CourseListFragment;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_course_list)
public class CourseListActivity extends BaseActivity {


    @Override
    public void initViews() {
        CourseListFragment courseListFragment = CourseListFragment.newInstance(true, 2,
                getIntent().getStringExtra("title"));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.course_list_fragment, courseListFragment)
                .commit();

    }

    @Override
    public void initDatas() {

    }



}
