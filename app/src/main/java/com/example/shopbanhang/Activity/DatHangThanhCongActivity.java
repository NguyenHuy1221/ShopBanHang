package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.shopbanhang.R;

public class DatHangThanhCongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang_thanh_cong);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DatHangThanhCongActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}