package com.efendi.minipom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.efendi.minipom.history.HistoryActivity;
import com.efendi.minipom.history.HistoryModel;
import com.efendi.minipom.utils.FuelCalculatorUtil;
import com.efendi.minipom.product.DropdownProductAdapter;
import com.efendi.minipom.pad_number.NumberAdapter;
import com.efendi.minipom.pad_number.NumberModel;
import com.efendi.minipom.product.ProductModel;
import com.efendi.minipom.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements
        NumberAdapter.OnItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private SpotsDialog spotsDialog;

    //spinner
    private  Spinner spinner;
    private TextView cost;
    private TextView amount_liter;
    private EditText tvBuy;


    //warning
    public RelativeLayout parent_warning;
    public TextView text_minim_stok;

    private ConstraintLayout parent_view;

    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent_view = findViewById(R.id.parent_view);

        //warning
        parent_warning = findViewById(R.id.parent_warning);
        text_minim_stok = findViewById(R.id.text_minim_stok);
        ImageView ic_close = findViewById(R.id.ic_close);
        ic_close.setOnClickListener(view -> parent_warning.setVisibility(View.GONE));

        ImageView btn_history = findViewById(R.id.btn_history);
        btn_history.setOnClickListener(view -> {
            Intent intent =  new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        spinner = findViewById(R.id.spinner);
        cost = findViewById(R.id.tvCost);
        amount_liter = findViewById(R.id.tvAmountLiter);
        tvBuy = findViewById(R.id.tvBuy);
        spotsDialog = new SpotsDialog(this);
        spotsDialog.create();
        spotsDialog.setCancelable(false);

        getProduct();
        showNumber();

    }

    private void getProduct() {
        spotsDialog.show();
        ArrayList<ProductModel>  list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    ProductModel p = dataSnapshot1.getValue(ProductModel.class);
                    list.add(p);
                }
                spotsDialog.dismiss();

                // Set up dropdown
                setupSpinner(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "Opsss.... Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                Snackbar.make(parent_view, "Opsss.... Terjadi kesalahan", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    double harga, stok;
    String name;
    String product_id;
    public void setupSpinner(List<ProductModel> spinnerItems) {
        DropdownProductAdapter adapter = new DropdownProductAdapter(MainActivity.this, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductModel selectedItem = spinnerItems.get(position);
                String liter = selectedItem.getAmount_liter() + " Liter";
                amount_liter.setText(liter);
                cost.setText(Constants.rupiahFormatter(String.valueOf(selectedItem.getCost())));
                stok = Double.parseDouble(selectedItem.getAmount_liter());
                harga = Double.parseDouble(String.valueOf(selectedItem.getCost()));
                name = selectedItem.getName();
                product_id = String.valueOf(selectedItem.getProduct_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Logika jika tidak ada yang dipilih
                Log.e(TAG, "No seleceted");

            }
        });
    }

    private void showNumber() {
        RecyclerView recyclerView = findViewById(R.id.number);
        ArrayList<NumberModel> list= new ArrayList<>();
        list.add(new NumberModel("1"));
        list.add(new NumberModel("2"));
        list.add(new NumberModel("3"));
        list.add(new NumberModel("4"));
        list.add(new NumberModel("5"));
        list.add(new NumberModel("6"));
        list.add(new NumberModel("7"));
        list.add(new NumberModel("8"));
        list.add(new NumberModel("9"));
        list.add(new NumberModel("Delete"));
        list.add(new NumberModel("0"));
        list.add(new NumberModel("Enter"));

        NumberAdapter myAdapter = new NumberAdapter(getApplicationContext(), list, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onItemClick(View view, NumberModel item, int pos) {
        if (!running) {
            if (tvBuy.getText().toString().trim().length() >= 10 && item.getIndex().equalsIgnoreCase("Enter")) {
                prosesIsiBensin(harga, Double.parseDouble(Constants.backString(tvBuy.getText().toString().trim())), stok);
            } else if (tvBuy.getText().toString().trim().length() >= 10 && !item.getIndex().equalsIgnoreCase("Delete")) {
                Log.e(TAG, "Max " + item.getIndex());
            } else if (item.getIndex().equalsIgnoreCase("0")) {
                if (tvBuy.getText().toString().trim().length() == 0) {
                    Log.e(TAG, "0 first");
                } else {
                    String no = Constants.decimalFormatter(Constants.backString(tvBuy.getText().toString().trim())
                            + item.getIndex());
                    tvBuy.setText(no);
                }
            } else if (item.getIndex().equalsIgnoreCase("Delete")) {
                if (tvBuy.getText().toString().trim().length() == 0) {
                    Log.e(TAG, "Delete first");
                } else {
                    String modified = Constants.backString(tvBuy.getText().toString().trim()).substring(0,
                            Constants.backString(tvBuy.getText().toString().trim()).length() - 1);
                    if (!modified.isEmpty()) {
                        tvBuy.setText(Constants.decimalFormatter(modified));
                    } else {
                        tvBuy.setText(modified);
                    }
                }
            } else if (item.getIndex().equalsIgnoreCase("Enter")) {
                if (tvBuy.getText().toString().trim().length() == 0) {
//                Toast.makeText(this, "Masukkan nominal",Toast.LENGTH_SHORT).show();
                    Snackbar.make(parent_view, "Masukkan nominal", Snackbar.LENGTH_SHORT).show();
                } else if (stok < 1) {
//                Toast.makeText(this, "Tidak bisa transaksi, Stok bahan bakar tidak mencukupi",Toast.LENGTH_SHORT).show();
                    Snackbar.make(parent_view, "Tidak bisa transaksi, Stok bahan bakar tidak mencukupi", Snackbar.LENGTH_SHORT).show();
                } else if (Double.parseDouble(Constants.backString(tvBuy.getText().toString().trim())) < harga) {
//                Toast.makeText(this, "Tidak memenuhi Minimal pembelian",Toast.LENGTH_SHORT).show();
                    Snackbar.make(parent_view, "Tidak memenuhi Minimal pembelian", Snackbar.LENGTH_SHORT).show();
                } else {
                    prosesIsiBensin(harga, Double.parseDouble(Constants.backString(tvBuy.getText().toString().trim())), stok);
                }
            } else {
                String no = Constants.decimalFormatter(Constants.backString(tvBuy.getText().toString().trim())
                        + item.getIndex());
                tvBuy.setText(no);
            }
        }else{
            Snackbar.make(parent_view, "Proses pengisian sedang berjalan", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void prosesIsiBensin(double hargaPerLiter, double hargaBeli, double stok) {

        spotsDialog.show();
//        Toast.makeText(this, "Proses Pengisian",Toast.LENGTH_SHORT).show();
        FuelCalculatorUtil calculator = new FuelCalculatorUtil(harga);
        double liters = calculator.calculateLiters(hargaBeli);
//        Log.e(TAG,"Jumlah Pembelian: " + hargaBeli);
//        Log.e(TAG,"Jumlah liter yang didapat: " + liters);
//        Log.e(TAG,"Stok akhir: " + Constants.doubleFormater(stok - liters));
//        Log.e(TAG,"Harga per liter: " + hargaPerLiter);
        String beli;
        if (hargaBeli == (int) hargaBeli) {
            beli = String.valueOf((int) hargaBeli);
        } else {
            beli = String.valueOf(hargaBeli);
        }
        String harga;
        if (hargaPerLiter == (int) hargaPerLiter) {
            harga = String.valueOf((int) hargaPerLiter);
        } else {
            harga = String.valueOf(hargaPerLiter);
        }
        if (stok - liters < 0){
            Snackbar.make(parent_view, "Transaksi Gagal, Stok tidak mencukupi!", Snackbar.LENGTH_SHORT).show();
        }else {
            running = true;
            startFuelingAnimation( hargaBeli, harga, beli, liters + " Liter", (stok - liters));
        }
    }

    private void popUp(String harga, String amount, String liter, String formattedDate) {
        running = false;

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_print);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView cancel = dialog.findViewById(R.id.btnCancel);
        TextView print = dialog.findViewById(R.id.btnPrint);
        TextView tanggal = dialog.findViewById(R.id.Tanggal);
        TextView tvHarga = dialog.findViewById(R.id.Harga);
        TextView tvLiter = dialog.findViewById(R.id.Liter);
        TextView produk = dialog.findViewById(R.id.Produk);
        TextView beli = dialog.findViewById(R.id.Beli);

        tanggal.setText(formattedDate);
        beli.setText(Constants.rupiahFormatter(amount));
        tvHarga.setText(Constants.rupiahFormatter(harga));
        tvLiter.setText(liter);
        produk.setText(name);

        cancel.setText(MainActivity.this.getResources().getString(R.string.close));
        cancel.setOnClickListener(view -> {
            dialog.dismiss();
            tvBuy.setText("");
            Snackbar.make(parent_view, "Transaksi Selesai!", Snackbar.LENGTH_SHORT).show();
        });
        print.setOnClickListener(view -> {
            dialog.dismiss();
            tvBuy.setText("");
            Snackbar.make(parent_view, "Proses Print", Snackbar.LENGTH_SHORT).show();
        });
        dialog.show();

    }

    private void savingHistory(String harga, String amount, String liter, double stok_akhir) {
        String id = FirebaseDatabase.getInstance().getReference("history").push().getKey();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm:ss", new Locale("id", "ID"));
        String formattedDate = sdf.format(new Date());
        HistoryModel upload = new HistoryModel(id, amount, formattedDate, name, liter, harga);

        if (id != null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            auth.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Berhasil mengotentikasi
                            // FirebaseUser user = auth.getCurrentUser();

                            // Simpan data ke Firebase Realtime Database
                            DatabaseReference ref = database.getReference("history");
                            ref.child(id).setValue(upload).addOnCompleteListener(dataTask -> {
                                if (dataTask.isSuccessful()) {
                                    // Data berhasil disimpan
                                    Log.e("TAG", "Sukses saving data: ", dataTask.getException());
                                    popUp(harga, amount, liter , formattedDate);
                                    updateStok(stok_akhir);
                                } else {
                                    // Gagal menyimpan data
                                    Log.e("TAG", "Error saving data: ", dataTask.getException());
                                }
                            });
                        } else {
                            // Gagal mengotentikasi
                            Log.e("TAG", "Error authenticating: ", task.getException());
                        }
                    });
        }
    }

    private void updateStok(double stok_akhir) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("product");
        try {
            ref.orderByChild("id").equalTo(product_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ref.child(product_id).child("amount_liter").setValue(String.valueOf(Constants.doubleFormater(stok_akhir)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startFuelingAnimation(double finalValue, String harga, String amount, String liter, double stok_akhir) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, (float) finalValue);
        valueAnimator.setDuration(5000); // animasi berlangsung selama 5 detik
        valueAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            String finalString = Constants.decimalFormatter(String.format("%.2f", animatedValue));
            tvBuy.setText(finalString);
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Tampilkan pesan ketika animasi selesai
                spotsDialog.dismiss();
                savingHistory(harga, amount, liter , stok_akhir);
            }
        });

        valueAnimator.start();
    }

}