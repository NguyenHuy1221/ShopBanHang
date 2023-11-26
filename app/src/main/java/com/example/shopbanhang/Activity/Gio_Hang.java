package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.shopbanhang.Adapter.GioHangAdapter;
import com.example.shopbanhang.Adapter.SanPhamAdapter;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Gio_Hang extends AppCompatActivity {

    private GioHangAdapter gioHangAdapter;
    private RecyclerView recyclerView;
    private GioHang gioHang;
    private DatabaseReference mDatabaseReference;
    private List<SanPham> sanPhams = new ArrayList<>();
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        gioHang = new GioHang();
        recyclerView = findViewById(R.id.rcy_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        getDataFirebase();
    }


    private void getDataFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("giohang");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham mSanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(mSanPham);
                }
                gioHangAdapter = new GioHangAdapter(sanPhams,context);
                recyclerView.setAdapter(gioHangAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}