package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shopbanhang.R;

public class SanPhamYeuThichMainActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView rcyTym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_yeu_thich_main);

        anhxa();
    }

    private void anhxa() {
        imgBack = findViewById(R.id.btnBack);
        imgBack.setOnClickListener(v -> finish());
        rcyTym = findViewById(R.id.rcyTym);
    }
}