package com.bs.teachassistant.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.entity.Less;
import com.bs.teachassistant.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limh on 2017/5/5.
 * 搜索
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {
    @ViewInject(R.id.list_search)
    ListView listSearch;
    @ViewInject(R.id.edit_search)
    EditText editSearch;
    @ViewInject(R.id.line_null)
    LinearLayout lineNull;

    private List<Less> allLess;
    private List<Less> resultLess;
    private CommDataAdapter<Less> adapter;

    @Override
    public void initViews() {
        resultLess = new ArrayList<>();
        allLess = new ArrayList<>();
        if (!TextUtils.isEmpty(userPreference.getString("allLess", ""))) {
            allLess.clear();
            allLess = GsonUtils.LessGsonToBean(userPreference.getString("allLess", ""));
        }
        if (allLess.size() > 0) {
            lineNull.setVisibility(View.GONE);
        } else {
            lineNull.setVisibility(View.VISIBLE);
        }

        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, AddLessActivity.class);
                intent.putExtra("less", resultLess.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initDatas() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String content = s.toString().trim();
                resultLess.clear();
                if (!TextUtils.isEmpty(content)) {
                    for (Less item : allLess) {
                        if (item.getName().contains(content) || item.getChapter().contains(content) ||
                                item.getContent().contains(content) || item.getTime().contains(content)) {
                            resultLess.add(item);
                        }
                    }
                }
                if (null == adapter) {
                    adapter = new CommDataAdapter<Less>(resultLess, R.layout.view_item_note) {
                        @Override
                        public void bindView(ViewHolder holder, Less obj) {
                            holder.setText(R.id.txt_note_title, obj.getName());
                            holder.setText(R.id.txt_note_time, obj.getTime());
                            holder.setText(R.id.txt_item_content, obj.getContent());
                            holder.setVisibility(R.id.txt_item_ramark, View.GONE);

                        }
                    };
                    listSearch.setAdapter(adapter);
                } else {
                    adapter.setmData(resultLess);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Event(value = R.id.image_search_close, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_search_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
        }
    }
}
