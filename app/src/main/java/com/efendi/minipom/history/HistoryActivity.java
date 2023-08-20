
package com.efendi.minipom.history;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.efendi.minipom.R;
import com.efendi.minipom.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnItemClickListener{

    private final String TAG = getClass().getSimpleName();
    private ConstraintLayout parent_view;
    private SpotsDialog spotsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setup();
        getHistory();
    }

    private void setup() {
        parent_view = findViewById(R.id.parent_view);

        spotsDialog = new SpotsDialog(this);
        spotsDialog.create();
        spotsDialog.setCancelable(false);
        spotsDialog.show();

        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getHistory() {
        RecyclerView recyclerView = findViewById(R.id.list_item);
        ArrayList<HistoryModel> list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history");
        Query query = reference.orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    HistoryModel p = dataSnapshot1.getValue(HistoryModel.class);
                    list.add(p);
                }
                Collections.reverse(list);
                spotsDialog.dismiss();
                //Set data ke RecyclerView
                HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), list, HistoryActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "Opsss.... Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                Snackbar.make(parent_view, "Opsss.... Terjadi kesalahan", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClick(View view, HistoryModel item, int pos) {
//        Log.e(TAG, item.getAmount() + "");
//        Log.e(TAG, Constants.rupiahFormatter(item.getPrice_per_liter()) + "");
        popUp(item);
    }

    private void popUp(HistoryModel item) {
        final Dialog dialog = new Dialog(HistoryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_print);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView cancel = dialog.findViewById(R.id.btnCancel);
        TextView print = dialog.findViewById(R.id.btnPrint);
        TextView tanggal = dialog.findViewById(R.id.Tanggal);
        TextView harga = dialog.findViewById(R.id.Harga);
        TextView liter = dialog.findViewById(R.id.Liter);
        TextView produk = dialog.findViewById(R.id.Produk);
        TextView beli = dialog.findViewById(R.id.Beli);

        tanggal.setText(item.getTime());
        beli.setText(Constants.rupiahFormatter(item.getAmount()));
        harga.setText(Constants.rupiahFormatter(item.getPrice_per_liter()));
        liter.setText(item.getAmount_liter());
        produk.setText(item.getType());

        cancel.setOnClickListener(view -> dialog.dismiss());
        print.setOnClickListener(view -> {
            dialog.dismiss();
            Snackbar.make(parent_view, "Proses Print", Snackbar.LENGTH_SHORT).show();
        });
        dialog.show();
    }


}