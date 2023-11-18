package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.shopbanhang.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class quan_ly_khuyen_mai extends AppCompatActivity {
    FloatingActionButton floatAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khuyen_mai);
        initView();
        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themKhuyenMai();
            }
        });
    }

    private void themKhuyenMai() {
        AlertDialog.Builder builder = new AlertDialog.Builder(quan_ly_khuyen_mai.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_khuyen_mai,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initView() {
        floatAdd = findViewById(R.id.floatAdd);
    }


}