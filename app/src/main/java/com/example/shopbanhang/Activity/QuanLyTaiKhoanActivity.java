package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.LayoutInflater;
import android.view.View;

import com.example.shopbanhang.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {
FloatingActionButton addtk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);
        initView();
        addtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themtk();
            }
        });
    }
    private void themtk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyTaiKhoanActivity.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_tai_khoan,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initView() {
        addtk = findViewById(R.id.addtk);
    }

}