package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.shopbanhang.Adapter.HoaDonAdapter;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.HoaDonPdf;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LichSuHoaDonMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HoaDonAdapter hoaDonAdapter;

    private List<HoaDon> mListhoadon = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_hoa_don_main);

        recyclerView = findViewById(R.id.rcvHoaDon);



        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("hoadon");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
//                    if (hoaDon != null) {
//                        String noiDungHoaDon = "Nội dung hóa đơn:\n" +
//                                "Ngày: " + hoaDon.getNgaytaoHD() + "\n" +
//                                "Số hóa đơn: " + hoaDon.getMaHD() + "\n" +
//                                "Sản phẩm: " + hoaDon.getSanPhamList() + "\n";
//
//                        Log.d("HUY", "Thông tin hóa đơn: " + noiDungHoaDon);
//                        String hoaDonPdf = "";
//                        HoaDonPdf.createPdf(noiDungHoaDon,hoaDonPdf);
//                    }
                    if (mListhoadon != null) {
                        mListhoadon.clear();
                    }
                    for (DataSnapshot list : snapshot.getChildren()) {
                        HoaDon hoaDon = list.getValue(HoaDon.class);

                        mListhoadon.add(hoaDon);
                    }
                    Log.i("b", "setAdapterHoaDon: " +mListhoadon.size());
                    //Set adapter
                    setAdapterHoaDon();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapterHoaDon() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        hoaDonAdapter = new HoaDonAdapter(mListhoadon);
        recyclerView.setAdapter(hoaDonAdapter);

    }

}