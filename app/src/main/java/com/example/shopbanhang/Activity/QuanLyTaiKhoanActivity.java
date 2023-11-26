package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.LayoutInflater;
import android.view.View;

import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Adapter.TaiKhoanAdapter;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {
FloatingActionButton addtk;
RecyclerView recycle_qltk;
TaiKhoanAdapter TaiKhoanAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);
        initView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycle_qltk.setLayoutManager(gridLayoutManager);
        TaiKhoanAdapter = new TaiKhoanAdapter(this,getList());
        recycle_qltk.setAdapter(TaiKhoanAdapter);
        addtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themtk();
            }
        });


    }
    private List<TaiKhoan> getList() {
        List<TaiKhoan> list = new ArrayList<>();
        list.add(new TaiKhoan("1","@drawable/h1.jpg","ngoquoctuan","123"));
        list.add(new TaiKhoan("2","@drawable/h1.jpg","hoduchau","123"));
        list.add(new TaiKhoan("3","@drawable/h1.jpg","phanhuythanh","123"));
        return list;
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
        recycle_qltk = findViewById(R.id.recycle_qltk);
    }

}