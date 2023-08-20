package com.efendi.minipom.history;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.efendi.minipom.R;
import com.efendi.minipom.utils.Constants;

import java.util.ArrayList;

public class HistoryAdapter extends  RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<HistoryModel> list;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, HistoryModel obj, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HistoryAdapter(Context context, ArrayList<HistoryModel> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView parent;
        private final TextView tvTanggal;
        private final TextView tvHarga;
        private final TextView tvLiter;
        private final TextView tvProduk;

        public ViewHolder(View v) {
            super(v);
            tvTanggal = v.findViewById(R.id.tvTanggal);
            tvHarga = v.findViewById(R.id.tvHarga);
            tvLiter = v.findViewById(R.id.tvLiter);
            tvProduk = v.findViewById(R.id.tvProduk);
            parent = v.findViewById(R.id.parent);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryAdapter.ViewHolder holder, final int position) {
        final HistoryModel item = list.get(position);
        holder.tvTanggal.setText(item.getTime());
        holder.tvHarga.setText(Constants.rupiahFormatter(item.getAmount()));
        holder.tvLiter.setText(item.getAmount_liter());
        holder.tvProduk.setText(item.getType());

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