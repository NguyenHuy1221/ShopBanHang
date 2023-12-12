package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shopbanhang.Adapter.SanPhamMainAdapter;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchMainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSearch;
    private EditText edtSearch;
    private SanPhamMainAdapter searchAdapter;
    private List<SanPham> allSanPham;
    private ImageView imgBack;

    private final Handler handler = new Handler();
    private final Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            filter(edtSearch.getText().toString().toLowerCase().trim());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        anhXa();
        setUpRecyclerView();

        allSanPham = new ArrayList<>();
        searchAdapter = new SanPhamMainAdapter(this, allSanPham);
        recyclerViewSearch.setAdapter(searchAdapter);

        getAllProducts();
    }

    private void anhXa() {
        edtSearch = findViewById(R.id.edtSearch);
        recyclerViewSearch = findViewById(R.id.ryc_search);
        imgBack = findViewById(R.id.imageView5);
        imgBack.setOnClickListener(v -> finish());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                filter(editable.toString().toLowerCase().trim());
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, 300);
            }
        });
    }

    private void setUpRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerViewSearch.setLayoutManager(gridLayoutManager);
    }

    private void getAllProducts() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allSanPham.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    allSanPham.add(sanPham);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filter(String query) {
        List<SanPham> filteredList = new ArrayList<>();
        for (SanPham sanPham : allSanPham) {
            if (sanPham.getTensp().toLowerCase().contains(query)) {
                filteredList.add(sanPham);
            }
        }


        searchAdapter.clear();

        if (query.isEmpty()) {
            filteredList.addAll(allSanPham);
        }

        searchAdapter.filterList(filteredList);
    }



}
