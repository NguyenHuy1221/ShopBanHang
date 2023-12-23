package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SanPhamyeuThichDAO {

    private DatabaseReference yeuthichref;

    public void deleteHoaDon(SanPham yeuThichSanPham) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("SanPhamYeuThich");
        myRef.child(String.valueOf(yeuThichSanPham.getMasp())).removeValue();
    }


}
