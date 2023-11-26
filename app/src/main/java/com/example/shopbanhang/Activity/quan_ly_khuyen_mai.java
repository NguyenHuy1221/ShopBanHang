package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class quan_ly_khuyen_mai extends AppCompatActivity {
    FloatingActionButton floatAdd;
    KhuyenMaiAdapter khuyenMaiAdapter;
    RecyclerView recycle_qlkm;

    ImageView backBtnKM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khuyen_mai);
        initView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycle_qlkm.setLayoutManager(gridLayoutManager);
        khuyenMaiAdapter = new KhuyenMaiAdapter(this,getList());
        recycle_qlkm.setAdapter(khuyenMaiAdapter);

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themKhuyenMai();
            }
        });
    }

    private List<KhuyenMai> getList() {
        List<KhuyenMai> list = new ArrayList<>();
        list.add(new KhuyenMai("1","hinh","Fall 2023","12/12/2023","1/1/2024","50%"));
        list.add(new KhuyenMai("2","hinh","Summer 2023","1/9/2023","1/1/2023","25%"));
        list.add(new KhuyenMai("3","hinh","Spring 2023","6/7/2023","1/10/2023","10%"));
        return list;
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
        recycle_qlkm = findViewById(R.id.recycle_qlkm);
        backBtnKM = findViewById(R.id.backBtnKM);
        backBtnKM.setOnClickListener(v -> finish());

    }


}