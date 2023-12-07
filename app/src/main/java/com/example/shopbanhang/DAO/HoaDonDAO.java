package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.HoaDon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HoaDonDAO {

    public void insertHoaDon(HoaDon HoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hoadon");
        myRef.child(HoaDon.getGiotaoHD()).setValue(HoaDon);
    }

    public void updateHoaDon(HoaDon HoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hoadon");
//        myRef.child(HoaDon.getGiotaoHD()).updateChildren(HoaDon.toMap());
    }

    public void deleteHoaDon(HoaDon HoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hoadon");
        myRef.child(HoaDon.getGiotaoHD()).removeValue();
    }

    public void updateStatus(String maHD, int newStatus) {
        DatabaseReference hoaDonRef = FirebaseDatabase.getInstance().getReference("hoadon").child(maHD);
        hoaDonRef.child("tinhTrang").setValue(newStatus);
    }
}
