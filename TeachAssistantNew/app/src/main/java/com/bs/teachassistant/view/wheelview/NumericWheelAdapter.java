package com.bs.teachassistant.view.wheelview;

import java.util.List;

/**
 * Created by limh on 2017/4/13.
 *
 */

public class NumericWheelAdapter implements WheelAdapter {
    private List<String> datas;

    public NumericWheelAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemsCount() {
        return datas.size();
    }

    @Override
    public String getItem(int index) {
        return datas.get(index);
    }

    @Override
    public int getMaximumLength() {
        return datas.size();
    }
}
