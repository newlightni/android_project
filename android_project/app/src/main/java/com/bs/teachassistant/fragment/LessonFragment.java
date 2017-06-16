package com.bs.teachassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.AddLessActivity;
import com.bs.teachassistant.adapter.TimeLessAdapter;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.database.LessDao;
import com.bs.teachassistant.database.NoteDao;
import com.bs.teachassistant.entity.Less;
import com.bs.teachassistant.entity.Note;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;
import com.bs.teachassistant.view.ItemClickListener;

import org.greenrobot.greendao.query.QueryBuilder;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/4/24.
 * 备课
 */
@ContentView(R.layout.fragment_lessons)
public class LessonFragment extends BaseFragment implements ItemClickListener {
    @ViewInject(R.id.txt_less_add)
    ImageView imageLessAdd;
    @ViewInject(R.id.line_less_add)
    LinearLayout lineLessAdd;
    @ViewInject(R.id.list_all_less)
    RecyclerView recyLess;

    private List<Less> allLess;
    private TimeLessAdapter adapter;
    private UndateLessBroadcast undateLessBroadcast;
    private Long courseId;

    public static LessonFragment newInstance(Long courseId) {
        LessonFragment lessonFragment = new LessonFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("courseId", courseId);
        lessonFragment.setArguments(bundle);
        return lessonFragment;
    }

    @Override
    public void initViews() {
        //注册更新课程广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_LESS);
        undateLessBroadcast = new UndateLessBroadcast();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(undateLessBroadcast, filter);

        recyLess.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyLess.setHasFixedSize(true);
        recyLess.setItemAnimator(new DefaultItemAnimator());

        allLess = new ArrayList<>();
        courseId = getArguments().getLong("courseId", 0);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onResume() {
        super.onResume();

        //allLess=lessDao.loadAll();

        QueryBuilder qb = lessDao.queryBuilder();
        qb.where(LessDao.Properties.CourseId.eq(courseId));
        List list = qb.list();
        allLess.clear();
        for (Object item : list) {
            LogUtil.d("addNote",item.toString());
            allLess.add((Less) item);
        }

        if (allLess.size() > 0) {
            lineLessAdd.setVisibility(View.GONE);
            imageLessAdd.setVisibility(View.VISIBLE);
        } else {
            lineLessAdd.setVisibility(View.VISIBLE);
            imageLessAdd.setVisibility(View.INVISIBLE);
        }

        if (null == adapter) {
            adapter = new TimeLessAdapter(getContext(), allLess);
            adapter.setItemClickListener(this);
            recyLess.setAdapter(adapter);
        } else {
            adapter.setDatas(allLess);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), AddLessActivity.class);
        intent.putExtra("less", allLess.get(position));
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Event(value = {R.id.txt_less_add, R.id.line_less_add}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_less_add:
            case R.id.line_less_add:
                Intent intent = new Intent(getActivity(), AddLessActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
        }
    }

    private class UndateLessBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_LESS:
                    initDatas();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != undateLessBroadcast) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(undateLessBroadcast);
        }
    }
}
