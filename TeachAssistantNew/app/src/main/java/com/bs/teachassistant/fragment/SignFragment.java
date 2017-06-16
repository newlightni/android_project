package com.bs.teachassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.AddStudActivity;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.common.Comm;
import com.bs.teachassistant.entity.StudBean;
import com.bs.teachassistant.service.ResourceAssetsHander;
import com.bs.teachassistant.service.WebConfig;
import com.bs.teachassistant.service.WebHttpServer;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import static com.bs.teachassistant.utils.FormatUtils.getSDKVersionNumber;

/**
 * Created by limh on 2017/4/24.
 * 签到模块
 */
@ContentView(R.layout.fragment_sign)
public class SignFragment extends BaseFragment {
    @ViewInject(R.id.line_sign)
    LinearLayout lineSign;
    @ViewInject(R.id.line_sign_status)
    LinearLayout lineStatus;
    @ViewInject(R.id.image_check)
    ImageView imageCheck;
    @ViewInject(R.id.txt_ip)
    TextView txtIp;
    @ViewInject(R.id.list_sign)
    ListView listSign;

    //签到服务器
    private WebHttpServer mHttpServer;
    private UpdateSignStudBroadcast updateSignStudBroadcast;
    private ArrayList<StudBean> studDatas;
    private CommDataAdapter<StudBean> adapter;

    private String TAG = "SignFragment";

    @Override
    public void initViews() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Comm.ACTION_UPDATE_ADD_SIGN);
        updateSignStudBroadcast = new UpdateSignStudBroadcast();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(updateSignStudBroadcast, filter);
        studDatas = new ArrayList<>();
    }

    @Override
    public void initDatas() {
        LogUtil.d(TAG, "刷新界面");
        if (studDatas.size() > 0) {
            lineStatus.setVisibility(View.VISIBLE);
        } else {
            lineStatus.setVisibility(View.INVISIBLE);
        }
        if (null == adapter) {
            adapter = new CommDataAdapter<StudBean>(studDatas, R.layout.view_item_sign) {
                @Override
                public void bindView(ViewHolder holder, StudBean obj) {
                    holder.setText(R.id.txt_id, "" + (studDatas.indexOf(obj) + 1));
                    holder.setText(R.id.txt_sign_id, obj.getStuId());
                    holder.setText(R.id.txt_sign_name, obj.getName());
                    holder.setText(R.id.txt_sign_status, obj.getStatus());
                    holder.setText(R.id.txt_sign_time, obj.getSignTime());
                    holder.setText(R.id.txt_sign_class, obj.getGroup());
                    holder.setText(R.id.txt_sign_term, obj.getTerm());
                    holder.setText(R.id.txt_sign_course, obj.getCourse());
                    if (obj.getStatus().equals("请假")) {
                        holder.setTextColor(R.id.txt_sign_status, Color.RED);
                    }else {
                        holder.setTextColor(R.id.txt_sign_status, Color.GRAY);
                    }
                }
            };
            listSign.setAdapter(adapter);
        } else {
            adapter.setmData(studDatas);
        }
    }

    @Event(value = {R.id.btn_copy, R.id.btn_finish, R.id.txt_start,
            R.id.btn_resign, R.id.txt_add_info, R.id.image_check}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_copy:
                if (getSDKVersionNumber() >= 11) {
                    android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(txtIp.getText().toString());
                } else {
                    // 得到剪贴板管理器
                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, txtIp.getText().toString()));
                }
                Toast.makeText(getActivity(), "签到地址已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_finish:
                lineSign.setVisibility(View.VISIBLE);
                mHttpServer.stopServer();
                break;
            case R.id.txt_start:
                if (TextUtils.isEmpty(userPreference.getString("students", ""))) {
                    Toast.makeText(getActivity(), "请先添加学生信息", Toast.LENGTH_SHORT).show();
                    break;
                }
                lineSign.setVisibility(View.GONE);
                txtIp.setText("http://" + getIp() + ":" + 8088);
                //启动手机端签到服务器
                WebConfig config = new WebConfig();
                config.setPort(8088);//端口
                config.setMaxParallels(100);//最大访问人数
                mHttpServer = new WebHttpServer(config);
                mHttpServer.registerResourceHander(new ResourceAssetsHander(getActivity()));
                mHttpServer.startServer();
                break;
            case R.id.btn_resign:
                userPreference.edit().putString("allStud", "").apply();
                Toast.makeText(getActivity(), "重置成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.txt_add_info:
                Intent intent = new Intent();
                intent.putExtra("type", "add");
                intent.setClass(getActivity(), AddStudActivity.class);
                studDatas.clear();
                adapter.clear();
                startActivity(intent);
                break;
            case R.id.image_check:
                Intent intent1 = new Intent();
                intent1.putExtra("type", "check");
                intent1.setClass(getActivity(), AddStudActivity.class);
                startActivity(intent1);
                break;

        }
    }

    /**
     * @return 本机IP地址
     */
    private String getIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 签到广播监听
     */
    private class UpdateSignStudBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Comm.ACTION_UPDATE_ADD_SIGN:
                    imageCheck.setVisibility(View.VISIBLE);
                    StudBean stud = intent.getParcelableExtra("stud");
                    studDatas.add(stud);
                    userPreference.edit().putString("signs", GsonUtils.GsonString(studDatas)).apply();
                    LogUtil.w(TAG, "stud" + stud.getStuId() + "  " + stud.getStatus());
                    initDatas();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != updateSignStudBroadcast)
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(updateSignStudBroadcast);
    }
}
