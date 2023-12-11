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

import com.bumptech.glide.Glide;
import com.example.shopbanhang.Model.ChiTietTaiKhoan;
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
    TextView sanpham,taikhoan,hoadon,doanhthu,khuyenmai,lichsumua,thongtinchitiet,doimatkhau,dangxuat;
    LinearLayout trangchu,yeuthich,giohang;
    private ImageView imgChinh;
    private TextView txtName,txtDiaChi;
    ImageView backBtn;
    private String user, email;
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

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = mySharedPreferences.getValue("remember_username_ten");
        email = mySharedPreferences.getValue("remember_username");

        txtName.setText(user);
        txtDiaChi.setText(email);


//        HienThiThongTin();



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

                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.clear();
                editor.commit();



                Intent intent = new Intent(Trang_Ca_Nhan.this, LoginActivity.class);
                startActivity(intent);
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
        DatabaseReference myRef = database.getReference("chiTietTaiKhoan");
        DatabaseReference userRef = myRef.child(email.replace(".", "_"));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    ChiTietTaiKhoan chiTietTaiKhoan = snapshot.getValue(ChiTietTaiKhoan.class);

                    txtName.setText(chiTietTaiKhoan.getName());
                    txtDiaChi.setText(chiTietTaiKhoan.getEmail());
                    Picasso.get().load(chiTietTaiKhoan.getUrlImage()).into(imgChinh);
                } else {
                    Log.d("Trang_Ca_Nhan", "Dữ liệu không tồn tại");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Trang_Ca_Nhan", "Lỗi đọc dữ liệu", error.toException());
            }
        });
    }


}