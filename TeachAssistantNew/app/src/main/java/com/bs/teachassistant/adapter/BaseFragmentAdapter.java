package com.bs.teachassistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bs.teachassistant.fragment.BaseFragment;

import java.util.List;


/**
 * Created by limh on 2017/2/10.
 * 基本Fragment适配器
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;
    private int size;

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
        size = fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }
}
