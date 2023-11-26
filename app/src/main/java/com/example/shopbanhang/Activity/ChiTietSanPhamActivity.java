package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopbanhang.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private ImageView imgPic,imgback;
    private TextView txtName,txtPrice,txtTitle;
    private Button btnadd;
    private Context context = this;
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
        imgback = findViewById(R.id.backBtn);
        imgback.setOnClickListener(v -> finish());
        btnadd = findViewById(R.id.btnAdd);
        btnadd.setOnClickListener(v -> addToCart());
    }

    public void getIntentSanPham(){
        Intent intent = getIntent();
        String tenSP = intent.getStringExtra("tensp");
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String ghiChu = intent.getStringExtra("ghichu");
        String imageUrl = intent.getStringExtra("imageUrl");

        txtName.setText(tenSP);
        txtPrice.setText(giaban + " VND ");
        txtTitle.setText(ghiChu);
        Picasso.get().load(imageUrl).into(imgPic);

    }
    public void addToCart(){
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

        txtTien.setText(giaban+"đ");
        Picasso.get().load(imageUrl).into(imgPic);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}