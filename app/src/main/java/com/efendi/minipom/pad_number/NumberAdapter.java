package com.efendi.minipom.pad_number;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.efendi.minipom.R;

import java.util.ArrayList;

public class NumberAdapter extends  RecyclerView.Adapter<NumberAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<NumberModel> list;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, NumberModel obj, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NumberAdapter(Context context, ArrayList<NumberModel> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView parent;
        private final TextView tvNumber;

        public ViewHolder(View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tvNumber);
            parent = v.findViewById(R.id.parent);
        }
    }

    @NonNull
    @Override
    public NumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NumberAdapter.ViewHolder holder, final int position) {
        final NumberModel item = list.get(position);
        String index = item.getIndex();
        holder.tvNumber.setText(index);
        if (index.equalsIgnoreCase("Delete")){
            holder.tvNumber.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvNumber.setTextSize(13);
            holder.parent.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }else if (index.equalsIgnoreCase("Enter")){
            holder.tvNumber.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvNumber.setTextSize(13);
            holder.parent.setCardBackgroundColor(context.getResources().getColor(R.color.blue_light));
        }

        holder.parent.setOnClickListener(v1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v1, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

}