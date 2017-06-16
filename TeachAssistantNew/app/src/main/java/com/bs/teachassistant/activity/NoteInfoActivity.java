package com.bs.teachassistant.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.adapter.CommDataAdapter;
import com.bs.teachassistant.database.LessDao;
import com.bs.teachassistant.database.NoteDao;
import com.bs.teachassistant.entity.Note;
import com.bs.teachassistant.utils.GsonUtils;
import com.bs.teachassistant.utils.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by limh on 2017/5/3.
 */
@ContentView(R.layout.activity_note_info)
public class NoteInfoActivity extends BaseActivity {
    @ViewInject(R.id.list_note)
    ListView listNote;
    @ViewInject(R.id.txt_info_title)
    TextView txtTitle;
    @ViewInject(R.id.line_null)
    LinearLayout lineNull;
    @ViewInject(R.id.edit_search)
    EditText editSearch;
    @ViewInject(R.id.note_info_add)
    ImageView noteInfoAdd;

    private CommDataAdapter<Note> adapter;
    private List<Note> currentNotes;
    private List<Note> searchNotes;
    private String title;

    @Override
    public void initViews() {
        title = getIntent().getStringExtra("classitfy");
        txtTitle.setText(title);
        currentNotes = new ArrayList<>();
        searchNotes = new ArrayList<>();
    }

    @Override
    public void initDatas() {
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(NoteInfoActivity.this, AddRecordActivity.class);
                intent.putExtra("note", currentNotes.get(i));
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    searchNotes.clear();
                    //搜索
                    for (Note item : currentNotes) {
                        if (item.getCalssName().contains(str) || item.getContent().contains(str) ||
                                item.getLocal().contains(str) || item.getRemark().contains(str) ||
                                item.getTitle().contains(str)) {
                            searchNotes.add(item);
                        }
                    }
                    if (null == adapter) {
                        adapter = new CommDataAdapter<Note>(searchNotes, R.layout.view_item_note) {
                            @Override
                            public void bindView(ViewHolder holder, Note obj) {
                                holder.setText(R.id.txt_note_title, obj.getTitle());
                                holder.setText(R.id.txt_note_time, obj.getTime());
                                holder.setText(R.id.txt_item_ramark, obj.getRemark());
                                holder.setText(R.id.txt_item_content, obj.getContent());

                                if (TextUtils.isEmpty(obj.getRemark()))
                                    holder.setVisibility(R.id.txt_item_ramark, View.GONE);
                                else
                                    holder.setVisibility(R.id.txt_item_ramark, View.VISIBLE);
                            }
                        };
                        listNote.setAdapter(adapter);
                    } else {
                        adapter.setmData(searchNotes);
                    }
                } else {
                    if (currentNotes.size() > 0) {
                        if (null == adapter) {
                            adapter = new CommDataAdapter<Note>(currentNotes, R.layout.view_item_note) {
                                @Override
                                public void bindView(ViewHolder holder, Note obj) {
                                    holder.setText(R.id.txt_note_title, obj.getTitle());
                                    holder.setText(R.id.txt_note_time, obj.getTime());
                                    holder.setText(R.id.txt_item_ramark, obj.getRemark());
                                    holder.setText(R.id.txt_item_content, obj.getContent());

                                    if (TextUtils.isEmpty(obj.getRemark()))
                                        holder.setVisibility(R.id.txt_item_ramark, View.GONE);
                                    else
                                        holder.setVisibility(R.id.txt_item_ramark, View.VISIBLE);
                                }
                            };
                            listNote.setAdapter(adapter);
                        } else {
                            adapter.setmData(currentNotes);
                        }
                    } else {
                        lineNull.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        QueryBuilder qb = noteDao.queryBuilder();
        qb.where(NoteDao.Properties.Calssify.eq(title));
        currentNotes.clear();
        List list = qb.list();
        for (Object item : list) {
            LogUtil.d("addNote",item.toString());
            currentNotes.add((Note) item);
        }
        lineNull.setVisibility(View.GONE);
        listNote.setVisibility(View.VISIBLE);
        if (currentNotes.size() > 0) {
            if (null == adapter) {
                adapter = new CommDataAdapter<Note>(currentNotes, R.layout.view_item_note) {
                    @Override
                    public void bindView(ViewHolder holder, Note obj) {
                        holder.setText(R.id.txt_note_title, obj.getTitle());
                        holder.setText(R.id.txt_note_time, obj.getTime());
                        holder.setText(R.id.txt_item_ramark, obj.getRemark());
                        holder.setText(R.id.txt_item_content, obj.getContent());

                        if (TextUtils.isEmpty(obj.getRemark()))
                            holder.setVisibility(R.id.txt_item_ramark, View.GONE);
                        else
                            holder.setVisibility(R.id.txt_item_ramark, View.VISIBLE);
                    }
                };
                listNote.setAdapter(adapter);
            } else {
                adapter.setmData(currentNotes);
            }
        } else {
            lineNull.setVisibility(View.VISIBLE);
        }
    }

    @Event(value = {R.id.image_info_close,R.id.note_info_add}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.image_info_close:
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;
            case R.id.note_info_add:
                Intent intent = new Intent();
                if(title.equals("便签记录")) {
                    intent.setClass(NoteInfoActivity.this, AddRecordActivity.class);
                } else {
                    intent.setClass(NoteInfoActivity.this, CourseListActivity.class);
                }
                intent.putExtra("title", title);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
        }
    }
}
