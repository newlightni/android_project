package com.bs.teachassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.ScheInfoActivity;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.service.RemindService;
import com.bs.teachassistant.utils.FormatUtils;
import com.bs.teachassistant.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bs.teachassistant.utils.FormatUtils.formatSuff;

/**
 * Created by limh on 2017/4/24.
 * 日程-近期活动
 */
@ContentView(R.layout.fragment_near)
public class NearFragment extends BaseFragment {
    @ViewInject(R.id.txt_null_1)
    TextView txtNull1;
    @ViewInject(R.id.txt_null_2)
    TextView txtNull2;
    @ViewInject(R.id.txt_null_3)
    TextView txtNull3;

    @ViewInject(R.id.list_1)
    ListView list1;
    @ViewInject(R.id.list_2)
    ListView list2;
    @ViewInject(R.id.list_3)
    ListView list3;

    private String day1, day2, day3;
    private List<ScheBean> scheData1;
    private List<ScheBean> scheData2;
    private List<ScheBean> scheData3;
    private CommDataAdapter<ScheBean> adapter1;
    private CommDataAdapter<ScheBean> adapter2;
    private CommDataAdapter<ScheBean> adapter3;
    private UpdateScheDataBroadcast updateScheDataBroadcast;

    @Override
    public void initViews() {
        scheData1 = new ArrayList<>();
        scheData2 = new ArrayList<>();
        scheData3 = new ArrayList<>();

        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        day1 = formatSuff(year) + "/" + formatSuff(month) + "/" + formatSuff(day - 1);
        day2 = formatSuff(year) + "/" + formatSuff(month) + "/" + formatSuff(day);
        day3 = formatSuff(year) + "/" + formatSuff(month) + "/" + formatSuff(day + 1);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_SCHE);
        updateScheDataBroadcast = new UpdateScheDataBroadcast();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(updateScheDataBroadcast, filter);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), ScheInfoActivity.class);
                intent.putExtra("sche",scheData1.get(i));
                startActivity(intent);
            }
        });
         list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), ScheInfoActivity.class);
                intent.putExtra("sche",scheData2.get(i));
                startActivity(intent);
            }
        });
         list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(getActivity(), ScheInfoActivity.class);
                intent.putExtra("sche",scheData3.get(i));
                startActivity(intent);
            }
        });

    }

    @Override
    public void initDatas() {
        List<ScheBean> allSche;
        if (!TextUtils.isEmpty(userPreference.getString("allSche", ""))) {
            allSche = GsonUtils.ScheGsonToBean(userPreference.getString("allSche", ""));
            for (ScheBean item : allSche) {
                if (FormatUtils.getLongTime(item.getEndTime().split(" ")[0] + " " + item.getEndTime().split(" ")[2]) < System.currentTimeMillis()) {
                    item.setFinish(true);
                }
            }
            userPreference.edit().putString("allSche",GsonUtils.GsonString(allSche)).apply();

            scheData1.clear();
            scheData2.clear();
            scheData3.clear();

            for (ScheBean item : allSche) {
                if (item.getStartTime().split(" ")[0].equals(day1))
                    scheData1.add(item);
                if (item.getStartTime().split(" ")[0].equals(day2))
                    scheData2.add(item);
                if (item.getStartTime().split(" ")[0].equals(day3))
                    scheData3.add(item);
            }
        }

        if (scheData1.size() > 0)
            txtNull1.setVisibility(View.GONE);
        else
            txtNull1.setVisibility(View.VISIBLE);

        if (scheData2.size() > 0)
            txtNull2.setVisibility(View.GONE);
        else{
            txtNull2.setVisibility(View.VISIBLE);
        }
        if (scheData3.size() > 0)
            txtNull3.setVisibility(View.GONE);
        else
            txtNull3.setVisibility(View.VISIBLE);

        if (null == adapter1) {
            adapter1 = new CommDataAdapter<ScheBean>(scheData1, R.layout.item_view_short) {
                @Override
                public void bindView(ViewHolder holder, ScheBean obj) {
                    holder.setText(R.id.txt_info_title, obj.getTitle());
                    holder.setText(R.id.txt_info_time, "开始时间：" + obj.getStartTime());
                    holder.setText(R.id.txt_status, obj.isFinish() ? "已完成" : "未完成");
                }
            };
            list1.setAdapter(adapter1);
        } else {
            adapter1.setmData(scheData1);
        }

        if (null == adapter2) {
            adapter2 = new CommDataAdapter<ScheBean>(scheData2, R.layout.item_view_short) {
                @Override
                public void bindView(ViewHolder holder, ScheBean obj) {
                    holder.setText(R.id.txt_info_title, obj.getTitle());
                    holder.setText(R.id.txt_info_time, "开始时间：" + obj.getStartTime());
                    holder.setText(R.id.txt_status, obj.isFinish() ? "已完成" : "未完成");
                }
            };
            list2.setAdapter(adapter2);
        } else {
            adapter2.setmData(scheData2);
        }

        if (null == adapter3) {
            adapter3 = new CommDataAdapter<ScheBean>(scheData3, R.layout.item_view_short) {
                @Override
                public void bindView(ViewHolder holder, ScheBean obj) {
                    holder.setText(R.id.txt_info_title, obj.getTitle());
                    holder.setText(R.id.txt_info_time, "开始时间：" + obj.getStartTime());
                    holder.setText(R.id.txt_status, obj.isFinish() ? "已完成" : "未完成");
                }
            };
            list3.setAdapter(adapter3);
        } else {
            adapter3.setmData(scheData3);
        }
    }

    private class UpdateScheDataBroadcast extends BroadcastReceiver {
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
        if (null == updateScheDataBroadcast)
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(updateScheDataBroadcast);
    }
}
