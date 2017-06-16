package com.bs.teachassistant.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/4/25.
 * 预览详情
 */
@ContentView(R.layout.activity_info)
public class ScheInfoActivity extends BaseActivity {

    @ViewInject(R.id.txt_info_time)
    TextView txtTime;
    @ViewInject(R.id.txt_info_local)
    TextView txtLocal;
    @ViewInject(R.id.txt_info_remark)
    TextView txtRemark;
    @ViewInject(R.id.txt_info_title)
    TextView txtTitle;
    @ViewInject(R.id.txt_info_status)
    TextView txtStatus;


    private ScheBean sche;
    private List<ScheBean> allSches;

    @Override
    public void initViews() {
        allSches = new ArrayList<>();
        if (!TextUtils.isEmpty(userPreference.getString("allSche", ""))) {
            allSches = GsonUtils.ScheGsonToBean(userPreference.getString("allSche", ""));
        }
        sche = getIntent().getParcelableExtra("sche");
    }

    @Override
    public void initDatas() {
        if (null != sche) {
            txtTitle.setText(sche.getTitle());
            txtTime.setText(sche.getStartTime() + "~" + sche.getEndTime());
            txtLocal.setText(sche.getLocal());
            txtRemark.setText(sche.getRemark());
            txtStatus.setText(sche.isFinish() ? "已完成" : "未完成");
        }
    }

    @Event(value = {R.id.txt_close, R.id.txt_delete}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.txt_close:
                finish();
                break;
            case R.id.txt_delete:
                List<ScheBean> temp = new ArrayList<>();
                for (ScheBean item : allSches) {
                    if (item.getTitle().equals(sche.getTitle()) && item.getStartTime().equals(sche.getStartTime()))
                        temp.add(item);
                }
                allSches.removeAll(temp);
                userPreference.edit().putString("allSche", GsonUtils.GsonString(allSches)).apply();
                Toast.makeText(ScheInfoActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Comm.ACTION_UPDATE_ADD_SCHE);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                finish();
                break;
        }
    }
}
