package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.HoaDon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HoaDonDAO {

    private DatabaseReference hoaDonRef;
//    public void insertHoaDon(HoaDon HoaDon) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("hoadon");
//        myRef.child(HoaDon.getGiotaoHD()).setValue(HoaDon);
//    }
//
//    public void updateHoaDon(HoaDon HoaDon) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("hoadon");
////        myRef.child(HoaDon.getGiotaoHD()).updateChildren(HoaDon.toMap());
//    }
//
    public void deleteHoaDon(HoaDon HoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("hoadon");
        myRef.child(String.valueOf(HoaDon.getMaHD())).removeValue();
    }

    public void updateStatus(HoaDon hoaDon) {
        hoaDonRef = FirebaseDatabase.getInstance().getReference("hoadon");
        hoaDonRef.child(String.valueOf(hoaDon.getMaHD())).child("tinhTrang").setValue(hoaDon.getTinhTrang());
    }
}
