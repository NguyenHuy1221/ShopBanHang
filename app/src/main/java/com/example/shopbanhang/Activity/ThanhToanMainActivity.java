package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.example.shopbanhang.Model.ChiTietGioHang;
import com.example.shopbanhang.Model.ChiTietHoaDon;
import com.example.shopbanhang.Model.ChiTietSanPhamfix;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ThanhToanMainActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtTongTien,txtGmail,txtSdt;
    private EditText edtDiaChi;
    private Button btn_mua_hang;
    private HoaDon hoaDon;
    double tien;
    private ArrayList<GioHang> gioHangList = new ArrayList<>(Gio_Hang.mlistGioHang);
    private ArrayList<ChiTietGioHang> chiTietGioHangArrayList = new ArrayList<>(Gio_Hang.mListChiTiet);
    private ArrayList<GioHang> list = new ArrayList<>();
    private ArrayList<SanPham> listsanpham = new ArrayList<>();

    private GioHang gioHang;
    private SanPham sanPham;

    private Context context = this ;
    private int idKhachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_main);

        anhxa();
        sendDataCart();


        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        idKhachHang = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
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
        getDataFirebasegiohang();
        btn_mua_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDiaChi = edtDiaChi.getText().toString().trim();
//                String id = UUID.randomUUID().toString();
                Random random = new Random();
                int id = random.nextInt(1000000);
                if (TextUtils.isEmpty(strDiaChi)){
                    Toast.makeText(ThanhToanMainActivity.this, "Bạn chưa nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                }else {
                    String DateToday = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String TimeToday = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//                    hoaDon = new HoaDon(id,user_name_login,DateToday,TimeToday,gioHangList,tien,trangthai);
                    hoaDon = new HoaDon(id,idKhachHang,DateToday,TimeToday,trangthai,0,strDiaChi,tien);
                    getDataFirebasesanpham();
                    addHoaDon(hoaDon);
                    clearGioHangData();
                    clearChiTietGioHangData();
                }
            }
        });
    }
    private ArrayList<Integer> listmasp = new ArrayList<>();
    private ArrayList<Integer> listmaspnumber = new ArrayList<>();
    private void getDataFirebasegiohang() {
        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("giohang");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (listmasp != null) {
                    listmasp.clear();
                }
                if (listmaspnumber != null) {
                    listmaspnumber.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

//                    DoanhThu doanhThu = dataSnapshot.getValue(DoanhThu.class);
                    gioHang = dataSnapshot.getValue(GioHang.class);
//                    if (gioHang != null){
//                        listmasp.add(gioHang.getMasp());
//                        listmaspnumber.add((gioHang.getSoluong()));
//                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDataFirebasesanpham() {
        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (listsanpham != null) {
                    listsanpham.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


//                    DoanhThu doanhThu = dataSnapshot.getValue(DoanhThu.class);
                    sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham != null){
                        Toast.makeText(ThanhToanMainActivity.this, ""+listmasp.size(), Toast.LENGTH_SHORT).show();

                        if (listmasp != null){
                            for (int i = 0; i < listmasp.size();i++){

                                if (sanPham.getMasp() == listmasp.get(i)){
                                    listsanpham.add(sanPham);
                                    int soluongdaban = 0;
                                    int soluongbancuagiohang;
                                    int soluongdabanbandau;
                                    soluongdabanbandau = sanPham.getSoluongban();
                                    soluongbancuagiohang = listmaspnumber.get(i);
                                    soluongdaban = soluongdabanbandau + soluongbancuagiohang;


                                    String pathObj = String.valueOf(sanPham.getMasp());
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("soluongban", soluongdaban);
                                    mDatabaseReference.child(pathObj).updateChildren(updates);
                                    soluongdaban = 0;

                                }
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

    private void sendDataCart() {
        Intent intent = getIntent();
        tien = intent.getDoubleExtra("tongtien",0);
        txtTongTien.setText(formatTien(tien) + " đ");
    }

    private void addHoaDon(HoaDon hoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hoaDonRef = database.getReference("hoadon");
        String TimeToday = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        hoaDonRef.child(TimeToday).setValue(hoaDon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addChiTietHoaDon(hoaDon.getMaHD());
                            Toast.makeText(ThanhToanMainActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ThanhToanMainActivity.this, "Lỗi khi thêm hóa đơn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void addChiTietHoaDon(int idHoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chiTietHoaDonRef = database.getReference("chitiethoadon");
        for (ChiTietGioHang chiTietGioHang : chiTietGioHangArrayList) {
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setId_chi_tiet_hoa_don(idHoaDon);
            chiTietHoaDon.setHoa_don(idHoaDon);
            chiTietHoaDon.setId_chi_tiet_san_pham(chiTietGioHang.getId_chi_tiet_san_pham());
            chiTietHoaDon.setSo_luong(chiTietGioHang.getSo_luong());
            chiTietHoaDon.setDon_gia(chiTietGioHang.getDon_gia());

            Random random = new Random();
            int idChiTietHoaDon = random.nextInt(1000);
            chiTietHoaDonRef.child(String.valueOf(idChiTietHoaDon)).setValue(chiTietHoaDon);
        }
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

    private void clearChiTietGioHangData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gioHangRef = database.getReference("chitietgiohang");

        gioHangRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chiTietGioHangArrayList.clear();
                startActivity(new Intent(ThanhToanMainActivity.this, TrangChuActivity.class));
            }
        });
    }

    private String formatTien(double value) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value) + " đ";
    }


}