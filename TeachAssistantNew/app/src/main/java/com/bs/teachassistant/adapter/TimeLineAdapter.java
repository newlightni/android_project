package com.bs.teachassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.entity.ScheBean;
import com.bs.teachassistant.view.ItemClickListener;

import java.util.List;

/**
 * Created by limh on 2017/3/10.
 * 时间记录Adapter
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {
    private Context context;
    private List<ScheBean> datas;
    ItemClickListener itemClickListener;

    public TimeLineAdapter(Context context, List<ScheBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_timeline, null);
        return new ViewHolder(v);
    }

    public void setDatas(List<ScheBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.viewLine1.setVisibility(View.INVISIBLE);
        } else {
            holder.viewLine1.setVisibility(View.VISIBLE);
        }

        if (position == datas.size() - 1) {
            holder.viewLine2.setVisibility(View.INVISIBLE);
        } else {
            holder.viewLine2.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(datas.get(position).getLocal())) {
            holder.txtLocal.setVisibility(View.INVISIBLE);
        } else {
            holder.txtLocal.setVisibility(View.VISIBLE);
            holder.txtLocal.setText(datas.get(position).getLocal());
        }

        holder.txtTitle.setText(datas.get(position).getTitle());
        holder.txtStatus.setText(datas.get(position).isFinish() ? "已完成" : "未开始");
        holder.txtStartTime.setText("开始：" + datas.get(position).getStartTime() + "\n结束：" + datas.get(position).getEndTime());
        holder.txtTime.setText(datas.get(position).getStartTime().split(" ")[0]);
        holder.txtContent.setText(datas.get(position).getRemark());

        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle;
        ImageView imageArrow;
        TextView txtTime;
        TextView txtContent;
        TextView txtLocal;
        TextView txtStartTime;
        TextView txtStatus;
        View viewLine1, viewLine2;


        public ViewHolder(View itemView) {
            super(itemView);
            imageArrow = (ImageView) itemView.findViewById(R.id.image_arrow);
            txtTime = (TextView) itemView.findViewById(R.id.txt_item_time);
            txtContent = (TextView) itemView.findViewById(R.id.txt_content);
            txtLocal = (TextView) itemView.findViewById(R.id.txt_local);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            viewLine1 = itemView.findViewById(R.id.view_line_1);
            viewLine2 = itemView.findViewById(R.id.view_line_2);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtStartTime = (TextView) itemView.findViewById(R.id.txt_time);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, (Integer) itemView.getTag());
            }
        }
    }
}
