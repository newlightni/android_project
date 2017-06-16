package com.bs.teachassistant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.AppConfig;
import com.bs.teachassistant.database.CourseDao;
import com.bs.teachassistant.database.LessDao;
import com.bs.teachassistant.database.NoteDao;
import com.bs.teachassistant.database.ScoreDao;

import org.xutils.x;

/**
 * Created by limh on 2017/4/23.
 * aicitivy基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    public AppConfig appConfig;
    public SharedPreferences userPreference;
    public LessDao lessDao;
    public NoteDao noteDao;
    public ScoreDao scoreDao;
    public CourseDao courseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        appConfig = AppConfig.getAppConfig();
        lessDao=appConfig.getDaoSession().getLessDao();
        noteDao=appConfig.getDaoSession().getNoteDao();
        scoreDao=appConfig.getDaoSession().getScoreDao();
        courseDao = appConfig.getDaoSession().getCourseDao();
        userPreference=getSharedPreferences("user",MODE_PRIVATE);
        initViews();
        initDatas();
        back();
    }

    public abstract void initViews();

    public abstract void initDatas();

    public void back() {
        ImageView imageView = (ImageView) findViewById(R.id.image_info_close);
        if(imageView == null) {

        }
        if(imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

}
