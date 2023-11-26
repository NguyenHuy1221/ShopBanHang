package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SanPhamDAO {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("sanpham");

    public void insertProducts(SanPham sanPham){
        myRef.child(String.valueOf(sanPham.getMasp())).setValue(sanPham);
    }

    public void updateProducts(SanPham sanPham) {
        myRef.child(String.valueOf(sanPham.getMasp())).updateChildren(sanPham.toMap());
    }

    public void deleteProducts(SanPham sanPham) {
        myRef.child(String.valueOf(sanPham.getMasp())).removeValue();
    }

}
