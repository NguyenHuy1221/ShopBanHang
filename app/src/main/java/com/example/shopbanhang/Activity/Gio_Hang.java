package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.CategoryMainAdapter;
import com.example.shopbanhang.Adapter.GioHangAdapter;
import com.example.shopbanhang.Adapter.SanPhamAdapter;
import com.example.shopbanhang.DAO.ThuongHieuDAO;
import com.example.shopbanhang.Model.ChiTietGioHang;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Gio_Hang extends AppCompatActivity {

    private GioHangAdapter gioHangAdapter;
    private RecyclerView recyclerView;
    public static List<GioHang> mlistGioHang;
    public static List<ChiTietGioHang> mListChiTiet = new ArrayList<>();
    private Context context = this;
    private ImageView btnBack,img_khuyen_mai;
    double tongTienSanPham = 0.0;
    private TextView txtTien;
    private TextView tvTienSanPham;
    private TextView tvGiamGia;
    private TextView tvTongTien;
    private TextView edtkhuyenmai;
    private Button btnMua;
    private int user;
    private final int[] idgiohang = {0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));

        anhxa();
        senDataCart();
        khuyenMai();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.rcy_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


    }

    private void anhxa() {
        btnBack = findViewById(R.id.btnBack);
        txtTien = findViewById(R.id.txtTongTien);
        btnMua = findViewById(R.id.button);
        img_khuyen_mai = findViewById(R.id.img_khuyen_mai);
        tvTienSanPham = findViewById(R.id.tv_tong_tien_hang);
        tvGiamGia = findViewById(R.id.tv_giam_gia);
        tvTongTien = findViewById(R.id.tv_tong_tien);
        edtkhuyenmai = findViewById(R.id.editTextText2);

        btnMua.setOnClickListener(v -> {
            Intent intent = new Intent(Gio_Hang.this, ThanhToanMainActivity.class);
            intent.putExtra("tongtien",tongTienSanPham);
            startActivity(intent);
        });
    }

    public void senDataCart(){
        mlistGioHang = new ArrayList<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("giohang");
        if (mlistGioHang != null){
            mlistGioHang.clear();
        }


        mDatabaseReference.orderByChild("id_khach_hang").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlistGioHang.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    GioHang gioHang = postSnapshot.getValue(GioHang.class);
                    mlistGioHang.add(gioHang);
                    senDataChiTietCart(gioHang.getId_gio_hang());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void senDataChiTietCart(int idGioHang) {
        DatabaseReference chiTietGioHangRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");

        Query query = chiTietGioHangRef.orderByChild("id_gio_hang").equalTo(idGioHang);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListChiTiet.clear();
                tongTienSanPham = 0.0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChiTietGioHang chiTietGioHang = dataSnapshot.getValue(ChiTietGioHang.class);
                    tongTienSanPham += chiTietGioHang.getTong_tien();

                    mListChiTiet.add(chiTietGioHang);
                    Log.d("HUY","tổng tiền " + formatTien(tongTienSanPham) );
                    tvTongTien.setText(formatTien(tongTienSanPham));
                    txtTien.setText(formatTien(tongTienSanPham));
                    tvTienSanPham.setText(formatTien(tongTienSanPham));
                    Log.d("HUY","tổng tiền đã tính : " + formatTien(tongTienSanPham) );
                    gioHangAdapter = new GioHangAdapter(context, mListChiTiet, new GioHangAdapter.IclickListener() {
                        @Override
                        public void onItemChanged(ChiTietGioHang chiTietGioHang) {
                            updateCart(chiTietGioHang);
                        }

                        @Override
                        public void onclickDeleteCart(ChiTietGioHang chiTietGioHang) {
                            deleteCart(chiTietGioHang);
                        }
                    });

                    recyclerView.setAdapter(gioHangAdapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteCart(ChiTietGioHang chiTietGioHang) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm ")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteChiTietGioHang(chiTietGioHang.getId_chi_tiet_gio_hang());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("chitietgiohang");
                        int keyToDelete = chiTietGioHang.getId_chi_tiet_gio_hang();

                        myRef.child(String.valueOf(keyToDelete)).removeValue()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        // Update the local list and notify the adapter
                                        mlistGioHang.remove(chiTietGioHang);
                                        gioHangAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setNegativeButton("Không", null).show();
    }


    private void deleteChiTietGioHang(int idGioHang) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chiTietGioHangRef = database.getReference("chitietgiohang");

        Query query = chiTietGioHangRef.orderByChild("id_gio_hang").equalTo(idGioHang);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lỗi khi xóa chi tiết giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateCart(ChiTietGioHang chiTietGioHang) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chitietgiohang").child(String.valueOf(chiTietGioHang.getId_chi_tiet_gio_hang()));
        myRef.setValue(chiTietGioHang).addOnCompleteListener(task -> {});
    }


    private void khuyenMai() {
        img_khuyen_mai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKhuyenMaiDialog();
            }
        });
    }

    private void showKhuyenMaiDialog() {
        DatabaseReference khuyenMaiRef = FirebaseDatabase.getInstance().getReference().child("KhuyenMai");
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        khuyenMaiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                        Date ngayketthuckm ;
                        Date ngayhomnay;
                        int result = 0;
                        String DateToday = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        try {
                            ngayketthuckm = sdf.parse(khuyenMai.getNgayKetThuc());
                            ngayhomnay = sdf.parse(DateToday);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        result = ngayhomnay.compareTo(ngayketthuckm);
                        if (khuyenMai != null) {
                                if (result < 0){
                                    danhSachKhuyenMai.add(khuyenMai);
                                }



                        }
                    }

                    String[] arrKhuyenMai = new String[danhSachKhuyenMai.size()];

                    for (int i = 0; i < danhSachKhuyenMai.size(); i++) {
                        arrKhuyenMai[i] = danhSachKhuyenMai.get(i).getTenKhuyenMai();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Chọn mã khuyến mãi");

                    // Danh sách chọn mã giảm giá
                    builder.setSingleChoiceItems(arrKhuyenMai, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            if (selectedPosition != -1) {
                                // Áp dụng mã giảm giá đã chọn
                                applySelectedKhuyenMai(danhSachKhuyenMai.get(selectedPosition));


                            }
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                    Log.d("KhuyenMai", "Danh sách khuyến mãi: " + danhSachKhuyenMai.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Lỗi khi truy xuất dữ liệu khuyến mãi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applySelectedKhuyenMai(KhuyenMai selectedKhuyenMai) {
        double tongGiaGoc = 0.0;
        double tongGiamGia = 0.0;

        // Tính tổng giá gốc của các sản phẩm trong giỏ hàng
//        for (ChiTietSanPham chiTietSanPham : mlistGioHang) {
//            tongGiaGoc += chiTietSanPham.get() * gioHang.getSoluong();
//        }
        edtkhuyenmai.setText(selectedKhuyenMai.getTenKhuyenMai());
        tongGiaGoc = tongTienSanPham;

        double tongGiaSauKhuyenMai = tongGiaGoc;

        // Áp dụng mã giảm giá đã chọn
        try {
            double phanTramKhuyenMai = Double.parseDouble(selectedKhuyenMai.getPhanTramKhuyenMai());

            // Áp dụng khuyến mãi đến tổng giá sau khuyến mãi và tính tổng giảm giá
            double giamGia = (tongGiaGoc * phanTramKhuyenMai) / 100;
            tongGiaSauKhuyenMai -= giamGia;
            tongGiamGia += giamGia;
        } catch (NumberFormatException e) {
            Log.e("NumberFormatException", "Không thể chuyển đổi thành số: " + selectedKhuyenMai.getPhanTramKhuyenMai());
        }

        tvGiamGia.setText(formatTien(tongGiamGia));
        tvTongTien.setText(formatTien(tongGiaSauKhuyenMai));
        txtTien.setText(formatTien(tongGiaSauKhuyenMai));

        double finalTongGiaSauKhuyenMai = tongGiaSauKhuyenMai;
        btnMua.setOnClickListener(v -> {
            Intent intent = new Intent(Gio_Hang.this, ThanhToanMainActivity.class);
            intent.putExtra("tongtien", finalTongGiaSauKhuyenMai);
            startActivity(intent);
        });
    }


    private String formatTien(double value) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value) + " đ";
    }


}