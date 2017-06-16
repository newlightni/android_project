package com.bs.teachassistant.activity;

import android.view.View;

import com.bs.teachassistant.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by limh on 2017/4/8.
 * 分享页面
 */
@ContentView(R.layout.activity_share)
public class ShareActivity extends BaseActivity {
    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }


    @Event(value = R.id.image_load_close, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            //返回按钮按下 关闭页面
            case R.id.image_load_close:
                finish();
                //页面关闭动画
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
        }
    }
}
