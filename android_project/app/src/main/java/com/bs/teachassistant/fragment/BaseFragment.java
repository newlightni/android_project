package com.bs.teachassistant.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.AppConfig;
import com.bs.teachassistant.database.CourseDao;
import com.bs.teachassistant.database.LessDao;
import com.bs.teachassistant.database.NoteDao;
import com.bs.teachassistant.database.ScoreDao;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.entity.StudBean;

import org.xutils.x;

/**
 * Created by limh on 2017/3/10.
 * fragment基类
 */

public abstract class BaseFragment extends Fragment {
    public AppConfig appConfig;
    public int screenWidth;
    public int screenHeigth;
    public SharedPreferences userPreference;
    public LessDao lessDao;
    public NoteDao noteDao;
    public CourseDao courseDao;
    public ScoreDao scoreDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appConfig = AppConfig.getAppConfig();
        lessDao=appConfig.getDaoSession().getLessDao();
        noteDao=appConfig.getDaoSession().getNoteDao();
        courseDao = appConfig.getDaoSession().getCourseDao();
        scoreDao = appConfig.getDaoSession().getScoreDao();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeigth = wm.getDefaultDisplay().getHeight();
        userPreference=getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    private boolean injected = false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
        initViews();
        initDatas();
    }

    public abstract void initViews();

    public abstract void initDatas();

    public void sendMsg(String msg, int what, Handler handler) {
        Bundle bundle = new Bundle();
        bundle.putString("regMsg", msg);
        Message message = new Message();
        message.setData(bundle);
        message.what = what;
        handler.sendMessage(message);
    }

}
