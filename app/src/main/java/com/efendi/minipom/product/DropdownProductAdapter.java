package com.efendi.minipom.product;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.efendi.minipom.MainActivity;
import com.efendi.minipom.R;

import java.util.List;

public class DropdownProductAdapter extends ArrayAdapter<ProductModel> {
    private final MainActivity context;
    private final List<ProductModel> items;

    public DropdownProductAdapter(@NonNull MainActivity context, List<ProductModel> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        this.context = context;
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dropdown_product, parent, false);
        }
        ProductModel item = items.get(position);

        String str_stok = item.getAmount_liter();
        double stok = Double.parseDouble(str_stok);
        if (stok > 0.0 && stok <= 2.00){
            context.parent_warning.setVisibility(View.VISIBLE);
            String name = item.getName() + " - " + context.getResources().getString(R.string.minimum_stok);
            context.text_minim_stok.setText(name);
        } else if (stok==0.0){
            context.parent_warning.setVisibility(View.VISIBLE);
            String name = item.getName() + " - " + context.getResources().getString(R.string.stok_kosong);
            context.text_minim_stok.setText(name);
        }
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(item.getName());

        return convertView;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = (TextView) convertView;
        ProductModel item = getItem(position);
        textView.setText(item.getName());

        return convertView;
    }
}