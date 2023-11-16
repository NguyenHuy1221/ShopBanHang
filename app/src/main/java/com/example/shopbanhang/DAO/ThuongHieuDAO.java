package com.example.shopbanhang.DAO;

import com.example.shopbanhang.Model.ThuongHieu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThuongHieuDAO {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("thuonghieu");

    public void insertThuongHieu(ThuongHieu thuongHieu){
        myRef.child(String.valueOf(thuongHieu.getIdThuongHieu())).setValue(thuongHieu);
    }

    public void updateThuongHieu(ThuongHieu thuongHieu) {
        myRef.child(String.valueOf(thuongHieu.getIdThuongHieu())).updateChildren(thuongHieu.toMap());
    }

    public void deleteThuongHieu(ThuongHieu thuongHieu) {
        myRef.child(thuongHieu.getIdThuongHieu()).removeValue();
    }

}
