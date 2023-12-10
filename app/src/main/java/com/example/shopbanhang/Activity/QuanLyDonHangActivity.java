package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.shopbanhang.Adapter.ChiTietDonHangAdapter;
import com.example.shopbanhang.Adapter.HoaDonAdapter;
import com.example.shopbanhang.Adapter.QuanLyDonHangAdapter;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuanLyDonHangActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuanLyDonHangAdapter quanLyDonHangAdapter;


    private List<HoaDon> mListhoadon = new ArrayList<>();
    private Context context = this;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_don_hang);

        recyclerView = findViewById(R.id.rcvHoaDon);

        DatabaseReference hoaDonMyRef = FirebaseDatabase.getInstance().getReference().child("hoadon");
        Query myRef = hoaDonMyRef.orderByChild("name_khachhang");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    if (mListhoadon != null) {
                        mListhoadon.clear();
                    }
                    for (DataSnapshot list : snapshot.getChildren()) {
                        HoaDon hoaDon = list.getValue(HoaDon.class);

                        mListhoadon.add(hoaDon);
                    }
                    Log.i("b", "setAdapterHoaDon: " +mListhoadon.size());
                    //Set adapter
                    setAdapterHoaDon();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapterHoaDon() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        quanLyDonHangAdapter = new QuanLyDonHangAdapter(context,mListhoadon);
        recyclerView.setAdapter(quanLyDonHangAdapter);
    }



}
