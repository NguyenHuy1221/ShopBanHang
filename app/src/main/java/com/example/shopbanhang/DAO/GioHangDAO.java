package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.GioHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GioHangDAO {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("sanpham");

    public void insertProducts(GioHang gioHang){
        myRef.child(String.valueOf(gioHang.getMasp())).setValue(gioHang);
    }

}
