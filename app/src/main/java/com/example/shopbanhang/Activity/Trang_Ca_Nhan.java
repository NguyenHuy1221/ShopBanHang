package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shopbanhang.R;

public class Trang_Ca_Nhan extends AppCompatActivity {
    TextView sanpham,taikhoan,hoadon,doanhthu,khuyenmai,lichsumua,thongtinchitiet,doimatkhau,dangxuat;
    LinearLayout trangchu,yeuthich,giohang;
    ImageView backBtn;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);
        sanpham = findViewById(R.id.GO_qlsp);
        taikhoan = findViewById(R.id.GO_qltk);
        hoadon = findViewById(R.id.GO_qlhd);
        doanhthu = findViewById(R.id.GO_qldt);
        khuyenmai = findViewById(R.id.GO_qlkm);
        backBtn = findViewById(R.id.backBtn);
        dangxuat = findViewById(R.id.GO_dangxuat);

        backBtn.setOnClickListener(v -> finish());
//        trangchu = findViewById(R.id.GO_trangchu);
//        yeuthich = findViewById(R.id.GO_yeuthich);
//        giohang = findViewById(R.id.GO_giohang);

        sanpham.setOnClickListener(v -> {
            Intent intent = new Intent(Trang_Ca_Nhan.this, QuanLySanPhamActivity.class);
            startActivity(intent);
        });

        taikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, QuanLyTaiKhoanActivity.class);
                startActivity(intent);
            }
        });

//        hoadon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Trang_Ca_Nhan.this, QuanLySanPhamActivity.class);
//                startActivity(intent);
//            }
//        });

        doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, Doanh_Thu.class);
                startActivity(intent);
            }
        });

        khuyenmai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, quan_ly_khuyen_mai.class);
                startActivity(intent);
            }
        });
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, LoginActivity.class);
                startActivity(intent);
            }
        });



//        trangchu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Trang_Ca_Nhan.this, TrangChuActivity.class);
//                startActivity(intent);
//            }
//        });
//        yeuthich.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Trang_Ca_Nhan.this, quan_ly_khuyen_mai.class);
//                startActivity(intent);
//            }
//        });
//        giohang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Trang_Ca_Nhan.this, Gio_Hang.class);
//                startActivity(intent);
//            }
//        });


    }
}