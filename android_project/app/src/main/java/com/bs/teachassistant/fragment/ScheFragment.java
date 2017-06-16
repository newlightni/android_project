package com.bs.teachassistant.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.bs.teachassistant.R;
import com.bs.teachassistant.activity.AddScheActivity;
import com.bs.teachassistant.adapter.BaseFragmentAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/4/24.
 * 日程
 */
@ContentView(R.layout.fragment_sche)
public class ScheFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.radioGroup_sche)
    RadioGroup radioGropSche;
    @ViewInject(R.id.vp_sche)
    ViewPager vpSche;

    List<BaseFragment> fragments;

    @Override
    public void initViews() {
        fragments = new ArrayList<>();
        fragments.add(new NearFragment());
        fragments.add(new WholeFragment());

        vpSche.setAdapter(new BaseFragmentAdapter(getChildFragmentManager(), fragments));
        vpSche.setCurrentItem(0);
        vpSche.addOnPageChangeListener(this);
    }

    @Override
    public void initDatas() {

    }

    /**
     * radioGroup按钮点击监听
     *
     * @param radioGroup RadioButton
     * @param i
     */
    @Event(value = R.id.radioGroup_sche, type = RadioGroup.OnCheckedChangeListener.class)
    private void OnCheckChange(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radio_sche_top_1:
                vpSche.setCurrentItem(0);
                break;
            case R.id.radio_sche_top_2:
                vpSche.setCurrentItem(1);
                break;
        }
    }

    /**
     * 添加日程按钮监听
     *
     * @param view 按钮
     */
    @Event(value = R.id.image_sche_add, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_sche_add:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddScheActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    /**
     * ViewPager页面滑动监听
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (vpSche.getCurrentItem()) {
                case 0:
                    radioGropSche.check(R.id.radio_sche_top_1);
                    break;
                case 1:
                    radioGropSche.check(R.id.radio_sche_top_2);
                    break;
            }
        }
    }
}
