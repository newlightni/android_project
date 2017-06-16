package com.bs.teachassistant.activity;


import com.bs.teachassistant.R;
import com.bs.teachassistant.fragment.LessonFragment;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_lessons)
public class LessonsActivity extends BaseActivity {


    @Override
    public void initViews() {

        LessonFragment lessonFragment = LessonFragment.newInstance(
                getIntent().getLongExtra("courseId", 0));
        getSupportFragmentManager().beginTransaction().add(
                R.id.lessons_fragment, lessonFragment).commit();

    }

    @Override
    public void initDatas() {

    }
}
