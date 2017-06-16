package com.bs.teachassistant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.adapter.CommDataAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/5/3.
 *
 */
@ContentView(R.layout.activity_check)
public class CheckClassActivity extends BaseActivity {
    @ViewInject(R.id.grid_note)
    GridView gridNote;

    private List<Card> cards;
    private CommDataAdapter<Card> adapter;

    @Override
    public void initViews() {
        cards = new ArrayList<>();
        cards.add(new Card(R.drawable.card_1, "教学日志"));
        cards.add(new Card(R.drawable.card_2, "授课总结"));
        cards.add(new Card(R.drawable.card_3, "便签记录"));
        cards.add(new Card(R.drawable.card_4, "平时成绩"));
        cards.add(new Card(R.drawable.card_5, "签到记录"));
        cards.add(new Card(R.drawable.card_6, "记录检索"));
        if (null == adapter) {
            adapter = new CommDataAdapter<Card>(cards, R.layout.view_grid_item) {
                @Override
                public void bindView(ViewHolder holder, Card obj) {
                    holder.setImageResource(R.id.image_card, obj.getResId());
                    holder.setText(R.id.txt_card, obj.getText());
                }
            };
            gridNote.setAdapter(adapter);
        } else {
            adapter.setmData(cards);
        }
    }

    @Override
    public void initDatas() {
        gridNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                        Intent intent = new Intent();
                        intent.setClass(CheckClassActivity.this, NoteInfoActivity.class);
                        intent.putExtra("classitfy", cards.get(i).getText());
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        break;
                    case 3:
                        Intent intent3 = new Intent();
                        intent3.putExtra("type", "update");
                        intent3.setClass(CheckClassActivity.this, AddScoreActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        break;
                    case 4:
                        Intent intent1 = new Intent();
                        intent1.putExtra("type", "check");
                        intent1.setClass(CheckClassActivity.this, AddStudActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        break;
                    case 5:
                        Intent intent2 = new Intent();
                        intent2.setClass(CheckClassActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        break;
                }

            }
        });
    }

    @Event(value = R.id.image_check_close, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_check_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
        }
    }

    private class Card {
        private int resId;
        private String text;

        Card(int resId, String text) {
            this.resId = resId;
            this.text = text;
        }

        public int getResId() {
            return resId;
        }

        public String getText() {
            return text;
        }
    }
}
