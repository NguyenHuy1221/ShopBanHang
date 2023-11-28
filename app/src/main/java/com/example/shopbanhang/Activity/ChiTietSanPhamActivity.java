package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.HienThiChiTietMain;
import com.example.shopbanhang.Adapter.ImageChiTietAdapter;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private ImageView imgPic, imgback;
    private TextView txtName, txtPrice, txtTitle;
    private Button btnadd;
    private Context context = this;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        anhXa();
        getIntentSanPham();

    }

    private void anhXa() {
        imgPic = findViewById(R.id.itemPic);
        txtName = findViewById(R.id.titleTxt);
        txtPrice = findViewById(R.id.priceTxt);
        txtTitle = findViewById(R.id.moTaTxt);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        imgback = findViewById(R.id.backBtn);
        imgback.setOnClickListener(v -> finish());
        btnadd = findViewById(R.id.btnAdd);
        btnadd.setOnClickListener(v -> addToCart());
    }

    public void getIntentSanPham() {
        Intent intent = getIntent();
        String tenSP = intent.getStringExtra("tensp");
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String ghiChu = intent.getStringExtra("ghichu");
        String imageUrl = intent.getStringExtra("imageUrl");

        if (intent.hasExtra("urlChiTiet")) {
            List<String> anhChiTiet = getIntent().getStringArrayListExtra("urlChiTiet");
            if (anhChiTiet != null && !anhChiTiet.isEmpty()) {
                HienThiChiTietMain adapter = new HienThiChiTietMain(context,anhChiTiet);
                recyclerView.setAdapter(adapter);
            }
        }else {

        }

        txtName.setText(tenSP);
        txtPrice.setText(giaban + " VND ");
        txtTitle.setText(ghiChu);
        Picasso.get().load(imageUrl).into(imgPic);
    }

    public void addToCart() {
        BottomSheetDialog dialog;
        View dialogsheetview = LayoutInflater.from(context).inflate(R.layout.dialog_add_to_cart, null);
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(dialogsheetview);
        dialog.show();

        ImageView imgPic = dialogsheetview.findViewById(R.id.itemPicCt);
        TextView txtTien = dialogsheetview.findViewById(R.id.txtMoney);
        Button btnadd = dialogsheetview.findViewById(R.id.btnAddCt);
        Intent intent = getIntent();
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");

        txtTien.setText(giaban + "đ");
        Picasso.get().load(imageUrl).into(imgPic);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passProductToCart();
                dialog.dismiss();
            }
        });
    }

    private void passProductToCart() {
        Intent intent = getIntent();
        String tenSP = intent.getStringExtra("tensp");
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String ghiChu = intent.getStringExtra("ghichu");
        String imageUrl = intent.getStringExtra("imageUrl");

        SanPham selectedProduct = new SanPham(System.currentTimeMillis(), generateUniqueProductId(), tenSP, 1, 0, 0.0, giaban, "", "", "", "", ghiChu, imageUrl, null
        );
        addToFirebase(selectedProduct);
        Toast.makeText(ChiTietSanPhamActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void addToFirebase(SanPham sanPham) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("giohang");
        databaseReference.child(String.valueOf(sanPham.getTimestamp())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Sản phẩm đã tồn tại, cập nhật số lượng
                    updateProductQuantity(sanPham);
                } else {
                    // Sản phẩm chưa tồn tại, thêm vào Firebase
                    databaseReference.child(String.valueOf(sanPham.getTimestamp())).setValue(sanPham);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProductQuantity(SanPham sanPham) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("giohang");

        databaseReference.child(String.valueOf(sanPham.getTimestamp())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin sản phẩm từ Firebase
                    SanPham existingProduct = dataSnapshot.getValue(SanPham.class);

                    // Tăng số lượng sản phẩm
                    int newQuantity = existingProduct.getSoluongnhap() + 1;

                    databaseReference.child(String.valueOf(sanPham.getTimestamp())).child("soluongnhap").setValue(newQuantity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private int generateUniqueProductId() {
        return (int) System.currentTimeMillis();
    }
}
