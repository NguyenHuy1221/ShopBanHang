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
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        anhxa();
        senDataCart();

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

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlistGioHang.clear();
                tongTienSanPham = 0.0;
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    GioHang gioHang = postSnapshot.getValue(GioHang.class);
                    gioHang.setKey(postSnapshot.getKey());
                    tongTienSanPham += gioHang.getTongtien();
                    txtTien.setText(tongTienSanPham+" đ");
                    tvTienSanPham.setText(tongTienSanPham+" đ");
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

            }
        });
    }


}