package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThanhToanMainActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtTongTien,txtGmail,txtSdt;
    private EditText edtDiaChi;
    private Button btn_mua_hang;
    private HoaDon hoaDon;
    double tien;
    private ArrayList<GioHang> gioHangList = new ArrayList<>(Gio_Hang.mlistGioHang);

    private Context context = this ;
    private String user_name_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_main);

        anhxa();
        sendDataCart();


        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user_name_login = mySharedPreferences.getValue("remember_username_ten");
    }

    private void anhxa() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        txtTongTien = findViewById(R.id.txtTongTien);
        txtGmail = findViewById(R.id.tvEmail);
        txtSdt = findViewById(R.id.tvSdt);
        edtDiaChi = findViewById(R.id.edtdiaChi);
        btn_mua_hang = findViewById(R.id.btn_mua_hang);
        int trangthai = 0;

        btn_mua_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDiaChi = edtDiaChi.getText().toString().trim();
                if (TextUtils.isEmpty(strDiaChi)){
                    Toast.makeText(ThanhToanMainActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else {
                    String DateToday = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String TimeToday = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    hoaDon = new HoaDon(TimeToday,user_name_login,DateToday,TimeToday,gioHangList,tien,trangthai);
                    addHoaDon(hoaDon);
                    clearGioHangData();
                }
            }
        });
    }

    private void sendDataCart() {
        Intent intent = getIntent();
        tien = intent.getDoubleExtra("tongtien",0);
        txtTongTien.setText(tien + " đ");
    }

    public void addHoaDon(HoaDon hoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hoaDonRef = database.getReference("hoadon");
        String TimeToday = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        hoaDonRef.child(TimeToday).setValue(hoaDon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ThanhToanMainActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearGioHangData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gioHangRef = database.getReference("giohang");

        gioHangRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                gioHangList.clear();
                startActivity(new Intent(ThanhToanMainActivity.this, TrangChuActivity.class));
            }
        });
    }

}