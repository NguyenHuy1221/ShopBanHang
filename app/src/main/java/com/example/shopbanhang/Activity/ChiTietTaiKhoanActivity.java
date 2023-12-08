package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChiTietTaiKhoanActivity extends AppCompatActivity {

    private ImageView imgName,imgEmail,imgDiaChi,imgSDT,imgPass;
    private TextView txtName,txtEmail,txtDiaChi,txtSDT,txtPass;
    private String user;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_tai_khoan);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = mySharedPreferences.getValue("remember_username_ten");
        Log.d("HUY","name :" + user);

        anhxa();
        getDataUser(user);
    }

    private void anhxa() {
        txtName = findViewById(R.id.txt_ten_chi_tiet);
        txtEmail = findViewById(R.id.txt_email_chi_tiet);
        txtDiaChi = findViewById(R.id.txt_dia_chi);
        txtSDT = findViewById(R.id.txt_sdt);
        txtPass = findViewById(R.id.txt_pass);
        imgName = findViewById(R.id.img_tenChiTiet);
        imgEmail = findViewById(R.id.img_emailChiTiet);
        imgDiaChi = findViewById(R.id.img_DiaChi);
        imgSDT = findViewById(R.id.img_SDTChiTiet);
        imgPass = findViewById(R.id.img_MKChiTiet);
    }

    private void getDataUser(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("TaiKhoan").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("ChiTietTaiKhoanActivity", "DataSnapshot exists");

                    String name = snapshot.child("tentk").getValue(String.class);
                    String email = snapshot.child("emailtk").getValue(String.class);
                    String diaChi = snapshot.child("diachitk").getValue(String.class);
                    String sdt = snapshot.child("sdttk").getValue(String.class);
                    String pass = snapshot.child("matkhautk").getValue(String.class);

                    Log.d("ChiTietTaiKhoanActivity", "Name: " + name);
                    Log.d("ChiTietTaiKhoanActivity", "Email: " + email);
                    Log.d("ChiTietTaiKhoanActivity", "DiaChi: " + diaChi);
                    Log.d("ChiTietTaiKhoanActivity", "SDT: " + sdt);
                    Log.d("ChiTietTaiKhoanActivity", "Pass: " + pass);

                    txtName.setText(name);
                    txtEmail.setText(email);
                    txtDiaChi.setText(diaChi);
                    txtSDT.setText(sdt);
                    txtPass.setText(pass);
                } else {
                    Log.d("ChiTietTaiKhoanActivity", "DataSnapshot does not exist for user ID: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChiTietTaiKhoanActivity", "Database Error: " + error.getMessage());
            }
        });
    }

}