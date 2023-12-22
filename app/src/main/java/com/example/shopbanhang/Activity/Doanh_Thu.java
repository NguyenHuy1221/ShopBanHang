package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.DoanhThuAdapter;
import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Model.ChiTietSanPhamfix;
import com.example.shopbanhang.Model.DoanhThu;
import com.example.shopbanhang.Model.HoaDon;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Doanh_Thu extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private ImageView imageViewt, imageViewd;
    private TextView txttongtien,txtt,txtd;
    private RecyclerView rclv;
    private DoanhThuAdapter doanhThuAdapter;
    private List<HoaDon> list = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    Button tinhtien;
    ImageView toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);
        getDataFirebase();
        rclv = findViewById(R.id.rclv_doanhthu);
        imageViewt = findViewById(R.id.imgLichT);
        imageViewd = findViewById(R.id.imgLichD);
        txtt = findViewById(R.id.txttungay);
        txtd = findViewById(R.id.txtdenngay);
        tinhtien = findViewById(R.id.btntinhtien);
        rclv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        doanhThuAdapter = new DoanhThuAdapter(Doanh_Thu.this, list );
        rclv.setAdapter(doanhThuAdapter);

        imageViewt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayBD();
                openCalendar();

            }
        });

        imageViewd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayKT();
                openCalendar();
            }
        });

        tinhtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtt.getText().toString().isEmpty() && txtd.getText().toString().isEmpty()){
                    Toast.makeText(Doanh_Thu.this, "Vui lòng chọn ngày tháng", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getDataFirebase();
                    Toast.makeText(Doanh_Thu.this, "Tổng doanh thu của bạn đây", Toast.LENGTH_SHORT).show();
                }


            }
        });
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(v -> finish());
    }




    private void getDataFirebase() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        double tongtien;
        txttongtien = findViewById(R.id.txt_tongdoanhthu);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hoadon");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (list != null) {
                    list.clear();
                }
                double tongtien = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Date ngaytaohd ;
                    Date datet;
                    Date dated;
                    int result = 0;
                    int result2 = 0;

//                    DoanhThu doanhThu = dataSnapshot.getValue(DoanhThu.class);
                    HoaDon doanhThu = dataSnapshot.getValue(HoaDon.class);
                    if (txtt.getText().toString() != null && txtd.getText().toString() != null) {
                        try {

                             ngaytaohd = sdf.parse(doanhThu.getNgaytaoHD());
                             datet = sdf.parse(txtt.getText().toString());
                             dated = sdf.parse(txtd.getText().toString());
                             result = ngaytaohd.compareTo(datet);
                             result2 = ngaytaohd.compareTo(dated);


                        } catch (ParseException e) {
//                            Toast.makeText(Doanh_Thu.this, "Lỗi chức năng doanh thu", Toast.LENGTH_SHORT).show();

                        }

                        if (result >= 0 && result2 <= 0) {
                            list.add(doanhThu);
                            rclv.setAdapter(doanhThuAdapter);

                                if (doanhThu.getTongtien() > 0){
                                    tongtien += doanhThu.getTongtien();

                                        txttongtien.setText(String.valueOf(tongtien));

                                }


                        }


                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year =  calendar.get(Calendar.YEAR);
        int month =  calendar.get(Calendar.MONTH);
        int day =  calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(Doanh_Thu.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setDayBD(){
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                txtt.setText(date);
            }
        };
    }

    private void setDayKT(){
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                txtd.setText(date);
            }
        };
    }
}