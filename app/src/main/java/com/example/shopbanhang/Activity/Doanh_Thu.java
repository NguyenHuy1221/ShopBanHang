package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopbanhang.Adapter.DoanhThuAdapter;
import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Model.DoanhThu;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Doanh_Thu extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private ImageView imageViewt, imageViewd;
    private TextView txttongtien;
    private RecyclerView rclv;
    private DoanhThuAdapter doanhThuAdapter;
    private List<DoanhThu> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);
        getDataFirebase();
        rclv = findViewById(R.id.rclv_doanhthu);
        rclv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        doanhThuAdapter = new DoanhThuAdapter(Doanh_Thu.this, list );
        rclv.setAdapter(doanhThuAdapter);
    }




    private void getDataFirebase() {


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hoadon");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoanhThu doanhThu = dataSnapshot.getValue(DoanhThu.class);
                        list.add(doanhThu);



                }
                rclv.setAdapter(doanhThuAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}