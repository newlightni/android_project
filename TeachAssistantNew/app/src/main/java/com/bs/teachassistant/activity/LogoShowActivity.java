package com.bs.teachassistant.activity;

import android.content.Intent;
import android.view.WindowManager;

import com.bs.teachassistant.R;
import com.bs.teachassistant.service.RemindService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by limh on 2017/4/25.
 *
 */

public class LogoShowActivity extends BaseActivity {
    private Timer mTimer;

    @Override
    public void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);
        mTimer = new Timer();
    }

    @Override
    public void initDatas() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(LogoShowActivity.this,RemindService.class);
                startService(intent);
                //每次App启动 重置签到表
                userPreference.edit().putString("allStud", "").apply();
                Intent intent2 = new Intent();
                intent2.setClass(LogoShowActivity.this, MainActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
