package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.Model.ChiTietTaiKhoan;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Trang_Ca_Nhan extends AppCompatActivity {
    TextView sanpham,taikhoan,hoadon,doanhthu,khuyenmai,lichsumua,thongtinchitiet,doimatkhau,dangxuat,cn_quan_ly,cn_mac_dinh;
    LinearLayout trangchu,yeuthich,giohang;
    private ImageView imgChinh;
    private TextView txtName,txtDiaChi;
    ImageView backBtn;
    private String user, email;
    int idTaiKhoan;
    private Context context = this;




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
        lichsumua = findViewById(R.id.GO_lichsumua);
        dangxuat = findViewById(R.id.GO_dangxuat);
        thongtinchitiet = findViewById(R.id.GO_chitiettaikhoan);
        imgChinh = findViewById(R.id.img_chinh);
        txtName = findViewById(R.id.txt_name);
        txtDiaChi = findViewById(R.id.txt_dia_chi);
        cn_quan_ly = findViewById(R.id.cn_quan_ly);
        cn_mac_dinh = findViewById(R.id.cn_mac_dinh);


        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = mySharedPreferences.getValue("remember_username_ten");
        email = mySharedPreferences.getValue("remember_username");
        idTaiKhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));



        boolean isAdmin = mySharedPreferences.getBooleanValue("permission_admin");
        Log.d("HUY","your admin " + isAdmin);

        txtName.setText(user);
        txtDiaChi.setText(email);


        HienThiThongTin();



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

        hoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, QuanLyDonHangActivity.class);
                startActivity(intent);
            }
        });

        lichsumua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, LichSuHoaDonMainActivity.class);
                startActivity(intent);
            }
        });

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
                FirebaseAuth.getInstance().signOut();
//              SharedPreferences mySharedPreferences = (SharedPreferences) new MySharedPreferences(Trang_Ca_Nhan.this);
//
//                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                editor.clear();
//                editor.commit();

                SharedPreferences mySharedPreferences = getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
                MySharedPreferences xoabonhotrong = new MySharedPreferences(Trang_Ca_Nhan.this);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.clear();
                editor.commit();

                SharedPreferences mySharedPreferences2 = getSharedPreferences("MY_PREFERENCES_BOOLEAN", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = mySharedPreferences2.edit();
                editor2.clear();
                editor2.commit();


                Toast.makeText(Trang_Ca_Nhan.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Trang_Ca_Nhan.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        thongtinchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trang_Ca_Nhan.this, ChiTietTaiKhoanActivity.class);
                startActivity(intent);
            }
        });


    }

    private void HienThiThongTin() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TaiKhoan");

        myRef.child(String.valueOf(idTaiKhoan)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);

                    if (isAdmin) {
                        sanpham.setVisibility(View.VISIBLE);
                        taikhoan.setVisibility(View.VISIBLE);
                        hoadon.setVisibility(View.VISIBLE);
                        doanhthu.setVisibility(View.VISIBLE);
                        lichsumua.setVisibility(View.VISIBLE);
                        khuyenmai.setVisibility(View.VISIBLE);
                        thongtinchitiet.setVisibility(View.VISIBLE);
                        cn_quan_ly.setVisibility(View.VISIBLE);
                    } else {
                        sanpham.setVisibility(View.GONE);
                        hoadon.setVisibility(View.GONE);
                        doanhthu.setVisibility(View.GONE);
                        khuyenmai.setVisibility(View.GONE);
                        taikhoan.setVisibility(View.GONE);
                        cn_quan_ly.setVisibility(View.GONE);
                        cn_mac_dinh.setVisibility(View.GONE);
                    }
                    String imgTaiKhoanUrl = dataSnapshot.child("imgtk").getValue(String.class);
                    if (imgTaiKhoanUrl != null && !imgTaiKhoanUrl.isEmpty()) {
                        Glide.with(context)
                                .load(imgTaiKhoanUrl)
                                .into(imgChinh);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }




}