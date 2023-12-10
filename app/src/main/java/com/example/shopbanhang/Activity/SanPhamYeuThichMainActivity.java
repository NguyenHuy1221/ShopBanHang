package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shopbanhang.Adapter.SanPhamMainAdapter;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SanPhamYeuThichMainActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView rcyTym;
    private SanPhamMainAdapter sanPhamYeuThichMainActivity;
    private List<SanPham> danhSachYeuThich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_yeu_thich_main);

        anhxa();

        // Khởi tạo danh sách sản phẩm yêu thích
        danhSachYeuThich = new ArrayList<>();
        sanPhamYeuThichMainActivity = new SanPhamMainAdapter(this, danhSachYeuThich);
        rcyTym.setAdapter(sanPhamYeuThichMainActivity);

        // Tải các sản phẩm yêu thích
//        sanPhamYeuThichMainActivity.loadFavoriteProducts();

        // Lấy tất cả sản phẩm từ nguồn dữ liệu của bạn
        List<SanPham> tatCaSanPham = layTatCaSanPham();

        // Lọc danh sách để chỉ lấy các sản phẩm yêu thích
//        List<SanPham> sanPhamYeuThich = laySanPhamYeuThich(tatCaSanPham);

        // Hiển thị các sản phẩm yêu thích
//        sanPhamYeuThichMainActivity.updateData(sanPhamYeuThich);
    }

    private void anhxa() {
        imgBack = findViewById(R.id.btnBack);
        imgBack.setOnClickListener(v -> finish());
        rcyTym = findViewById(R.id.rcyTym);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcyTym.setLayoutManager(layoutManager);
    }



    // Thay thế phương thức này bằng logic thực tế của bạn để lấy dữ liệu
    private List<SanPham> layTatCaSanPham() {
        // Logic của bạn để lấy tất cả sản phẩm từ nguồn dữ liệu
        return new ArrayList<>();
    }

//    private List<SanPham> laySanPhamYeuThich(List<SanPham> tatCaSanPham) {
//        List<SanPham> sanPhamYeuThich = new ArrayList<>();
//        for (SanPham sanPham : tatCaSanPham) {
//            String maSanPham = String.valueOf(sanPham.getMasp());
//            if (sanPhamYeuThichMainActivity.isFavorite(maSanPham)) {
//                sanPhamYeuThich.add(sanPham);
//            }
//        }
//        return sanPhamYeuThich;
//    }



}