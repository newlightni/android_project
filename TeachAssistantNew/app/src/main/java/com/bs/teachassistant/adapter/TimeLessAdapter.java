package com.bs.teachassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.entity.Less;
import com.bs.teachassistant.view.ItemClickListener;

import java.util.List;

/**
 * Created by limh on 2017/3/10.
 * 时间记录Adapter
 */

public class TimeLessAdapter extends RecyclerView.Adapter<TimeLessAdapter.ViewHolder> {
    private Context context;
    private List<Less> datas;
    ItemClickListener itemClickListener;

    public TimeLessAdapter(Context context, List<Less> datas) {
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

    public void setDatas(List<Less> datas) {
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


        holder.txtLocal.setVisibility(View.INVISIBLE);

        holder.txtTitle.setText(datas.get(position).getName());
        holder.txtStartTime.setText(datas.get(position).getTime());
        holder.txtTime.setText(datas.get(position).getTime().split(" ")[0]);
        holder.txtContent.setText(datas.get(position).getChapter());

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
