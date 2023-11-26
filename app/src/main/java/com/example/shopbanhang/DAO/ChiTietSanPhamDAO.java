package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChiTietSanPhamDAO {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("chitietsanpham");

    public void insertProducts(ChiTietSanPham chiTietSanPham){
        myRef.child(String.valueOf(chiTietSanPham.getMasp())).setValue(chiTietSanPham);
    }

}
