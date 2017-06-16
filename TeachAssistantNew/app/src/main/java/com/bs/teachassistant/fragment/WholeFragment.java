package com.bs.teachassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.ScheInfoActivity;
import com.bs.teachassistant.adapter.TimeLineAdapter;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.view.ItemClickListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/4/24.
 * 日程-全部日程
 */
@ContentView(R.layout.fragment_whole)
public class WholeFragment extends BaseFragment implements ItemClickListener {
    @ViewInject(R.id.list_all_richen)
    RecyclerView listRichen;
    @ViewInject(R.id.txt_richen_null)
    TextView txtNull;

    private TimeLineAdapter adapter;
    private List<ScheBean> scheDatas;
    private RefreshDataBroadCast refreshDataBroadCast = null;

    @Override
    public void initViews() {
        scheDatas = new ArrayList<>();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_SCHE);
        refreshDataBroadCast = new RefreshDataBroadCast();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshDataBroadCast, filter);

        listRichen.setLayoutManager(new LinearLayoutManager(getActivity()));
        listRichen.setHasFixedSize(true);
        listRichen.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initDatas() {
        String allSche = userPreference.getString("allSche", "");
        scheDatas.clear();
        if (!TextUtils.isEmpty(allSche)) {
            scheDatas = GsonUtils.ScheGsonToBean(allSche);
            for (ScheBean item : scheDatas) {
                if (FormatUtils.getLongTime(item.getEndTime().split(" ")[0] + " " + item.getEndTime().split(" ")[2]) < System.currentTimeMillis()) {
                    item.setFinish(true);
                }
            }
        }
        if (scheDatas.size() > 0) {
            txtNull.setVisibility(View.GONE);
        } else {
            txtNull.setVisibility(View.VISIBLE);
        }

        if (null == adapter) {
            adapter = new TimeLineAdapter(getContext(), scheDatas);
            adapter.setItemClickListener(this);
            listRichen.setAdapter(adapter);
        } else {
            adapter.setDatas(scheDatas);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ScheInfoActivity.class);
        intent.putExtra("sche", scheDatas.get(position));
        startActivity(intent);
    }

    private class RefreshDataBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_SCHE:
                    initDatas();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != refreshDataBroadCast)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshDataBroadCast);
    }
}
