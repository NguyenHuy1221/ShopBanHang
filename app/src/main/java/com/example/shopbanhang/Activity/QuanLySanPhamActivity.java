package com.example.shopbanhang.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.ImageChiTietAdapter;
import com.example.shopbanhang.Adapter.SanPhamAdapter;
import com.example.shopbanhang.DAO.ChiTietSanPhamDAO;
import com.example.shopbanhang.DAO.SanPhamDAO;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.ChiTietSanPhamfix;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class QuanLySanPhamActivity extends AppCompatActivity {

    private List<SanPham> sanPhams = new ArrayList<>();

    private FloatingActionButton floatingActionButton;
    private Context context = this;

    private SanPhamAdapter sanPhamAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabaseReference;
    private ImageView imgBack;
    private Spinner spnloaisp, spnchiso;
    private List<String> loaisp = new ArrayList<>();
    private List<String> chisosanpham = new ArrayList<>();
    private Button btn_loc;
    private List<SanPham> sanphamloc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);
        spnloaisp = findViewById(R.id.spn_loaisanpham);
        spnchiso = findViewById(R.id.spn_chiso);
        btn_loc = findViewById(R.id.btn_loc);

        imgBack = findViewById(R.id.toolbar);
        imgBack.setOnClickListener(v -> finish());

        floatingActionButton = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.rcy_qlsp);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        loaisp.add("Tất cả");
        loaisp.add("GIÀY");
        loaisp.add("ÁO");
        loaisp.add("QUẦN");
        loaisp.add("ÁO KHOÁC");

        ArrayAdapter<String> loaisanphamAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, loaisp);
        loaisanphamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnloaisp.setAdapter(loaisanphamAdapter);


        chisosanpham.add("Tùy chọn");
        chisosanpham.add("Bán được nhiều sản phẩm nhất");
        chisosanpham.add("Còn nhiều sản phẩm");
//        chisosanpham.add("Xanh");
//        chisosanpham.add("Đen");
//        chisosanpham.add("Đỏ");

        ArrayAdapter<String> chisosanphamAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, chisosanpham);
        chisosanphamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnchiso.setAdapter(chisosanphamAdapter);




        sanPhamAdapter = new SanPhamAdapter(context, sanPhams, new SanPhamAdapter.OnClickItem() {
            @Override
            public void onclikUpdate(SanPham sanPham) {
                EditProducts(sanPham);
            }

            @Override
            public void onclickDelete(SanPham sanPham) {
                DeleteProducts(sanPham);
            }
        });
        recyclerView.setAdapter(sanPhamAdapter);
        sanPhamAdapter.updateList(sanPhams);
        sanPhamAdapter.notifyDataSetChanged();

        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanphamloc.clear();
                      String spnloaispValue = spnloaisp.getSelectedItem().toString();
                      String spnchisoValue = spnchiso.getSelectedItem().toString();


                      switch (spnloaispValue) {
                        case "Tất cả":
                            switch (spnchisoValue){
                                case "Tùy chọn":
                                    for (int i= 0; i<sanPhams.size();i++){
                                        sanphamloc.add(sanPhams.get(i));
                                    }
                                    break;
                                case "Bán được nhiều sản phẩm nhất":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        sanphamloc.add(sanPhams.get(i));
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongban() - o1.getSoluongban();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;

                                case "Còn nhiều sản phẩm":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        sanphamloc.add(sanPhams.get(i));
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongnhap() - o1.getSoluongnhap();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;
                                default:
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                            }

                            break;
                        case "GIÀY":
                            switch (spnchisoValue){
                                case "Tùy chọn":
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                                case "Bán được nhiều sản phẩm nhất":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongban() - o1.getSoluongban();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;

                                case "Còn nhiều sản phẩm":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongnhap() - o1.getSoluongnhap();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;
                                default:
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                            }
                            break;
                        case "ÁO":
                            switch (spnchisoValue){
                                case "Tùy chọn":
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                                case "Bán được nhiều sản phẩm nhất":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongban() - o1.getSoluongban();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;

                                case "Còn nhiều sản phẩm":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongnhap() - o1.getSoluongnhap();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;
                                default:
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                            }
                            break;
                        case "QUẦN":
                            switch (spnchisoValue){
                                case "Tùy chọn":
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                                case "Bán được nhiều sản phẩm nhất":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongban() - o1.getSoluongban();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;

                                case "Còn nhiều sản phẩm":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongnhap() - o1.getSoluongnhap();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;
                                default:
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                            }
                            break;
                        case "ÁO KHOÁC":
                            switch (spnchisoValue){
                                case "Tùy chọn":
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                                case "Bán được nhiều sản phẩm nhất":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongban() - o1.getSoluongban();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;

                                case "Còn nhiều sản phẩm":
                                    for (int i= 0; i<sanPhams.size();i++){

                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                        Comparator<SanPham> comparator = (o1, o2) -> o2.getSoluongnhap() - o1.getSoluongnhap();
                                        Collections.sort(sanphamloc, comparator);
                                    }
                                    break;
                                default:
                                    for (int i= 0; i<sanPhams.size();i++){
                                        if (spnloaispValue.equals(sanPhams.get(i).getThuonghieu())){
                                            sanphamloc.add(sanPhams.get(i));
                                        }
                                    }
                                    break;
                            }
                            break;

                        default:
                            for (int i= 0; i<sanPhams.size();i++){
                                sanphamloc.add(sanPhams.get(i));
                            }
                            sanPhamAdapter.notifyDataSetChanged();
                            break;

                    }






                sanPhamAdapter = new SanPhamAdapter(context, sanphamloc, new SanPhamAdapter.OnClickItem() {
                    @Override
                    public void onclikUpdate(SanPham sanPham) {
                        EditProducts(sanPham);
                    }

                    @Override
                    public void onclickDelete(SanPham sanPham) {
                        DeleteProducts(sanPham);
                    }
                });
                recyclerView.setAdapter(sanPhamAdapter);
                sanPhamAdapter.updateList(sanphamloc);
                sanPhamAdapter.notifyDataSetChanged();
            }
        });
        getDataFirebase();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProducts();
            }
        });
    }
    private void getDataFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
//        DatabaseReference DatabaseReferenceChiTiet;
//        DatabaseReferenceChiTiet = FirebaseDatabase.getInstance().getReference("Chitietsanpham");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham mSanPham = dataSnapshot.getValue(SanPham.class);
                    // Tạo hai biến tạm thời để lưu giá trị của spnchiso và thuonghieu
//                    String spnchisoValue = spnloaisp.getSelectedItem().toString();
//                    String thuonghieuValue = mSanPham.getThuonghieu();
//
//                    Toast.makeText(QuanLySanPhamActivity.this, "thong tin so sanh"+spnchisoValue + thuonghieuValue , Toast.LENGTH_SHORT).show();
//
//                    switch (spnchisoValue) {
//                        case "Tất cả":
//                                sanPhams.clear();
//                                sanPhams.add(mSanPham);
//
//
//                            break;
//                        case "GIÀY":
//                            sanPhams.clear();
//                            if (mSanPham.getThuonghieu().equals(spnchisoValue)){
//                                sanPhams.add(mSanPham);
//                            }
//                            break;
//                        case "ÁO":
//                            sanPhams.clear();
//                            if (mSanPham.getThuonghieu().equals(spnchisoValue)){
//                                sanPhams.add(mSanPham);
//                            }
//                            break;
//                        case "QUẦN":
//                            sanPhams.clear();
//                            if (mSanPham.getThuonghieu().equals(spnchisoValue)){
//                                sanPhams.add(mSanPham);
//                            }
//                            break;
//                        case "ÁO KHOÁC":
//                            sanPhams.clear();
//                            if (mSanPham.getThuonghieu().equals(spnchisoValue)){
//                                sanPhams.add(mSanPham);
//                            }
//                            break;
//
//                        default:
//                            sanPhams.add(mSanPham);
//                            break;
//
//                    }
                    sanPhams.add(mSanPham);
                    sanPhamAdapter = new SanPhamAdapter(context, sanPhams, new SanPhamAdapter.OnClickItem() {
                        @Override
                        public void onclikUpdate(SanPham sanPham) {
                            EditProducts(sanPham);
                        }

                        @Override
                        public void onclickDelete(SanPham sanPham) {
                            DeleteProducts(sanPham);
                        }
                    });
                    recyclerView.setAdapter(sanPhamAdapter);
                    sanPhamAdapter.updateList(sanPhams);
                    sanPhamAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void getDataFirebase() {
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
////        DatabaseReference DatabaseReferenceChiTiet;
////        DatabaseReferenceChiTiet = FirebaseDatabase.getInstance().getReference("Chitietsanpham");
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sanPhams.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    SanPham mSanPham = dataSnapshot.getValue(SanPham.class);
//                    if (spnchiso.getSelectedItem().equals(loaisp.get(0))){
//                        if (mSanPham.getThuonghieu().equals(loaisp.get(0))){
//                            sanPhams.add(mSanPham);
//                        }
//
//                    } else if (spnchiso.getSelectedItem().equals(loaisp.get(1))){
//                        if (mSanPham.getThuonghieu().equals(loaisp.get(1))){
//                            sanPhams.add(mSanPham);
//                        }
//
//                    } else if (spnchiso.getSelectedItem().equals(loaisp.get(2))){
//                        if (mSanPham.getThuonghieu().equals(loaisp.get(2))){
//                            sanPhams.add(mSanPham);
//                        }
//
//                    }
//
//
//                }
//                    sanPhamAdapter = new SanPhamAdapter(context, sanPhams, new SanPhamAdapter.OnClickItem() {
//                        @Override
//                        public void onclikUpdate(SanPham sanPham) {
//                            EditProducts(sanPham);
//                        }
//
//                        @Override
//                        public void onclickDelete(SanPham sanPham) {
//                            DeleteProducts(sanPham);
//                        }
//                    });
//                    recyclerView.setAdapter(sanPhamAdapter);
//                    sanPhamAdapter.updateList(sanPhams);
//                sanPhamAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    private List<ChiTietSanPhamfix> chiTietSanPhams = new ArrayList<>();
    private void DeleteProducts(SanPham sanPham) {

        new AlertDialog.Builder(this)
                .setTitle("Shop Quần Áo")
                .setMessage("Xác Nhận Xóa ??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("sanpham");

                        DatabaseReference myRef2 = database.getReference("Chitietsanpham");
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Chitietsanpham");
                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (chiTietSanPhams != null) {
                                    chiTietSanPhams.clear();
                                }
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ChiTietSanPhamfix chiTietSanPham = dataSnapshot.getValue(ChiTietSanPhamfix.class);
                                    if (chiTietSanPham != null){
                                        if (chiTietSanPham.getMasp() == sanPham.getMasp()){
//                                            if (chiTietSanPham.getIdchitietsanpham() != null){
//                                                chiTietSanPhams.add(chiTietSanPham);

                                            myRef2.child(String.valueOf(chiTietSanPham.getIdchitietsanpham())).removeValue();
                                                sanPhamAdapter.notifyDataSetChanged();
//                                            }

                                        }
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        myRef.child(String.valueOf(sanPham.getMasp())).removeValue();

                    }
                })

                .setNegativeButton("Cancel",null)
                .show();

    }

    private void EditProducts(SanPham sanPham) {
        Intent intent = new Intent(QuanLySanPhamActivity.this, SuaSanPhamActivity.class);
        intent.putExtra("SAN_PHAM", sanPham);
        intent.putExtra("masanpham", sanPham.getMasp());
        intent.putExtra("anhSanPham", sanPham.getImageUrl());

        List<String> urlChiTiet = sanPham.getUrlChiTiet();
        if (urlChiTiet!= null){
            intent.putStringArrayListExtra("urlChiTiet",new ArrayList<>(urlChiTiet));
        }

        startActivity(intent);
        finish();
    }



    private void addProducts() {

        Intent intent = new Intent(QuanLySanPhamActivity.this, ThemSanPhamActivity.class);
        startActivity(intent);
    }
}