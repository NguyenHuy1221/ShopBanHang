package com.example.shopbanhang.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Adapter.SanPhamMainAdapter;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.YeuThichSanPham;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SanPhamYeuThichMainActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView rcyTym;

    private DatabaseReference databaseReference;
    private List<YeuThichSanPham> mListYeuThich;
    private SanPhamMainAdapter sanPhamMainAdapter;

    private int user;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_yeu_thich_main);

        anhXa();

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));

        getDataSanPhamYeuThich();
    }

    private void anhXa() {
        imgBack = findViewById(R.id.btnBack);
        imgBack.setOnClickListener(v -> finish());
        rcyTym = findViewById(R.id.rcyTym);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        rcyTym.setLayoutManager(gridLayoutManager);

        mListYeuThich = new ArrayList<>();
    }

    private void getDataSanPhamYeuThich() {
        mListYeuThich = new ArrayList<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("SanPhamYeuThich");

        mDatabaseReference.orderByChild("id_khach_hang").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListYeuThich.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    YeuThichSanPham yeuThichSanPham = postSnapshot.getValue(YeuThichSanPham.class);
                    mListYeuThich.add(yeuThichSanPham);
                }

                if (sanPhamMainAdapter == null) {
                    sanPhamMainAdapter = new SanPhamMainAdapter(context, new ArrayList<>());
                    rcyTym.setAdapter(sanPhamMainAdapter);
                }

                for (YeuThichSanPham yeuThichSanPham : mListYeuThich) {
                    getSanPhamInfo(yeuThichSanPham.getId_san_pham());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void getSanPhamInfo(int idSanPham) {
        DatabaseReference sanPhamRef = FirebaseDatabase.getInstance().getReference("sanpham").child(String.valueOf(idSanPham));

        sanPhamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    sanPhamMainAdapter.addSanPham(sanPham);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
