package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.ChiTietDonHangAdapter;
import com.example.shopbanhang.Adapter.HoaDonAdapter;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.HoaDonPdf;
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

public class LichSuHoaDonMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoaDonAdapter hoaDonAdapter;
    private ChiTietDonHangAdapter chiTietDonHangAdapter;

    private List<HoaDon> mListhoadon = new ArrayList<>();
    private Context context = this;
    private int user;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_hoa_don_main);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));

        recyclerView = findViewById(R.id.rcvHoaDon);
        Log.d("HUY","Tài khoản : " + user);

        DatabaseReference hoaDonMyRef = FirebaseDatabase.getInstance().getReference().child("hoadon");
        Query myRef = hoaDonMyRef.orderByChild("idKhachHang").equalTo(user);
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
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LichSuHoaDonMainActivity.this, TrangChuActivity.class);
//                startActivity(intent);
//
//            }
//        });
    }

    private void setAdapterHoaDon() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        hoaDonAdapter = new HoaDonAdapter(context,mListhoadon);
        recyclerView.setAdapter(hoaDonAdapter);
    }



}