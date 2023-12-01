package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.HienThiChiTietMain;
import com.example.shopbanhang.Adapter.ImageChiTietAdapter;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private ImageView imgPic, imgback;
    private TextView txtName, txtPrice, txtTitle;
    private Button btnadd;
    private Context context = this;
    private RecyclerView recyclerView;
    public static List<GioHang> mGioHangToFirebase;
    private String Color = null;
    private String Size = null;
    private int so = 1;
    BottomSheetDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        mGioHangToFirebase = new ArrayList<>();

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
                HienThiChiTietMain adapter = new HienThiChiTietMain(context, anhChiTiet, new HienThiChiTietMain.OnItemClickListener() {
                    @Override
                    public void onItemClick(String imageUrl) {
                        Picasso.get().load(imageUrl).into(imgPic);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }

        txtName.setText(tenSP);
        txtPrice.setText(giaban + " đ ");
        txtTitle.setText(ghiChu);
        Picasso.get().load(imageUrl).into(imgPic);
    }



    public void addToCart() {

        View dialogsheetview = LayoutInflater.from(context).inflate(R.layout.dialog_add_to_cart, null);
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(dialogsheetview);
        dialog.show();

        ImageView imgPic = dialogsheetview.findViewById(R.id.itemPicCt);
        TextView txtTien = dialogsheetview.findViewById(R.id.txtMoney);
        TextView txtCong = dialogsheetview.findViewById(R.id.tvCong);
        TextView txtSo = dialogsheetview.findViewById(R.id.tvSo);
        TextView txtTru = dialogsheetview.findViewById(R.id.tvtru);
        Button btnadd = dialogsheetview.findViewById(R.id.btnAddCt);
        Intent intent = getIntent();
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");

        txtTien.setText(giaban + "đ");
        Picasso.get().load(imageUrl).into(imgPic);

        setBackGroundColor(dialogsheetview);
        setBackgroundSize(dialogsheetview);

        txtTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (so > 1) {
                    so--;
                    txtSo.setText(String.valueOf(so));
                }
            }
        });

        txtCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                so++;
                txtSo.setText(String.valueOf(so));
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });
    }

    private void addProductToCart() {
        Intent intent = getIntent();
        int masp = intent.getIntExtra("masp",0);
        String tenSP = intent.getStringExtra("tensp");
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");

        double tongTien = giaban * so;

        if (Color == null || Size == null) {
            Toast.makeText(context, "Chưa chọn màu hoặc size", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("giohang");

        String newKey = myRef.push().getKey();

        myRef.child(newKey).setValue(new GioHang(masp, tenSP, giaban, so, imageUrl, Size, Color,tongTien))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Color = null;
                        Size = null;
                    } else {
                        Toast.makeText(context, "Lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setBackGroundColor(View dialogsheetview) {
        CardView colorBlack = dialogsheetview.findViewById(R.id.color_black);
        CardView colorWhite = dialogsheetview.findViewById(R.id.color_white);
        CardView colorOrange = dialogsheetview.findViewById(R.id.color_orange);
        CardView colorBlue = dialogsheetview.findViewById(R.id.color_blue);

        colorWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorWhite.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_imageview));
                colorBlack.setBackground(null);
                colorBlue.setBackground(null);
                colorOrange.setBackground(null);
                Color = "Trắng";
            }
        });
        colorBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorBlack.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_imageview));
                colorWhite.setBackground(null);
                colorBlue.setBackground(null);
                colorOrange.setBackground(null);
                Color = "Đen";

            }
        });
        colorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorBlue.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_imageview));
                colorBlack.setBackground(null);
                colorWhite.setBackground(null);
                colorOrange.setBackground(null);
                Color = "Xanh";
            }
        });
        colorOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorOrange.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_imageview));
                colorBlack.setBackground(null);
                colorBlue.setBackground(null);
                colorWhite.setBackground(null);
                Color = "Cam";
            }
        });

    }

    private void setBackgroundSize(View dialogsheetview) {
        TextView sizeM = dialogsheetview.findViewById(R.id.sizeM);
        TextView sizeL = dialogsheetview.findViewById(R.id.sizeL);
        TextView sizeXL = dialogsheetview.findViewById(R.id.sizeXl);
        TextView sizeXXL = dialogsheetview.findViewById(R.id.sizeXXL);
        sizeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeM.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                sizeL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeXL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeXXL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                Size = "M";
            }
        });
        sizeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeL.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                sizeM.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeXL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeXXL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                Size = "L";
            }
        });
        sizeXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeXL.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                sizeM.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeXXL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                Size = "XL";

            }
        });
        sizeXXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeXXL.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
                sizeM.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                sizeXL.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                Size = "XL";

            }
        });
    }
}
