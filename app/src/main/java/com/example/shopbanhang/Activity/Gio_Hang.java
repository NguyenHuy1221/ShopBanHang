package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Gio_Hang extends AppCompatActivity {

    private GioHangAdapter gioHangAdapter;
    private RecyclerView recyclerView;
    public static List<GioHang> mlistGioHang;
    private Context context = this;
    private ImageView btnBack,img_khuyen_mai;
    double tongTienSanPham = 0.0;
    private TextView txtTien,tvTienSanPham,tvGiamGia,tvTongTien;
    private Button btnMua;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = mySharedPreferences.getValue("remember_username_ten");

        anhxa();
        senDataCart();
        khuyenMai();

        btnMua.setOnClickListener(v -> {
            Intent intent = new Intent(Gio_Hang.this, ThanhToanMainActivity.class);
            intent.putExtra("tongtien",tongTienSanPham);
            startActivity(intent);
        });
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
    }

    public void senDataCart(){
        mlistGioHang = new ArrayList<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("giohang");

        mDatabaseReference.orderByChild("user").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlistGioHang.clear();
                tongTienSanPham = 0.0;
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    GioHang gioHang = postSnapshot.getValue(GioHang.class);
                    gioHang.setKey(postSnapshot.getKey());
                    tongTienSanPham += gioHang.getTongtien();
                    tvTongTien.setText(formatTien(tongTienSanPham));
                    txtTien.setText(formatTien(tongTienSanPham));
                    tvTienSanPham.setText(formatTien(tongTienSanPham));
                    mlistGioHang.add(gioHang);
                }
                gioHangAdapter = new GioHangAdapter(context, mlistGioHang, new GioHangAdapter.IclickListener() {
                    @Override
                    public void onItemChanged(GioHang gioHang) {
                        updateCart(gioHang);
//                        updateTongTien();
                    }

                    @Override
                    public void onclickDeleteCart(GioHang gioHang) {
                        deleteCart(gioHang);
                    }
                });
                recyclerView.setAdapter(gioHangAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteCart(GioHang gioHang) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm ")
                .setMessage("Bạn có chắc muốn xóa san phẩm này")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("giohang");
                        String keyToDelete = gioHang.getKey();

                        myRef.child(keyToDelete).removeValue()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        // Tuỳ chọn, bạn cũng có thể xóa mục khỏi danh sách cục bộ
                                        mlistGioHang.remove(gioHang);
                                        gioHangAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setNegativeButton("Không", null).show();
    }

    private void updateCart(GioHang gioHang) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("giohang").child(gioHang.getKey());
        myRef.setValue(gioHang).addOnCompleteListener(task -> {});
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

        khuyenMaiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                        if (khuyenMai != null) {
                            danhSachKhuyenMai.add(khuyenMai);
                        }
                    }

                    String[] arrKhuyenMai = new String[danhSachKhuyenMai.size()];
                    final boolean[] selectedItems = new boolean[danhSachKhuyenMai.size()];

                    for (int i = 0; i < danhSachKhuyenMai.size(); i++) {
                        arrKhuyenMai[i] = danhSachKhuyenMai.get(i).getTenKhuyenMai();
                        selectedItems[i] = false;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Chọn mã khuyến mãi");

                    builder.setMultiChoiceItems(arrKhuyenMai, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            selectedItems[which] = isChecked;
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            applySelectedKhuyenMai(danhSachKhuyenMai, selectedItems);
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

    private void applySelectedKhuyenMai(List<KhuyenMai> danhSachKhuyenMai, boolean[] selectedItems) {
        double tongGiaGoc = 0.0;
        double tongGiamGia = 0.0;

        // Tính tổng giá gốc của các sản phẩm trong giỏ hàng
        for (GioHang gioHang : mlistGioHang) {
            tongGiaGoc += gioHang.getGiasp() * gioHang.getSoluong();
        }

        double tongGiaSauKhuyenMai = tongGiaGoc;

        // Áp dụng khuyến mãi đã chọn
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                // Chuyển đổi phần trăm khuyến mãi từ String sang số
                KhuyenMai khuyenMai = danhSachKhuyenMai.get(i);
                try {
                    double phanTramKhuyenMai = Double.parseDouble(khuyenMai.getPhanTramKhuyenMai());

                    // Áp dụng khuyến mãi đến tổng giá sau khuyến mãi và tính tổng giảm giá
                    double giamGia = (tongGiaGoc * phanTramKhuyenMai) / 100;
                    tongGiaSauKhuyenMai -= giamGia;
                    tongGiamGia += giamGia;
                } catch (NumberFormatException e) {
                    Log.e("NumberFormatException", "Không thể chuyển đổi thành số: " + khuyenMai.getPhanTramKhuyenMai());
                }
            }
        }

        // Hiển thị tổng giảm giá trên tvGiamGia
        tvGiamGia.setText(formatTien(tongGiamGia));
        tvTongTien.setText(formatTien(tongGiaSauKhuyenMai));
        txtTien.setText(formatTien(tongGiaSauKhuyenMai));

    }
    private String formatTien(double value) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value) + " đ";
    }
}