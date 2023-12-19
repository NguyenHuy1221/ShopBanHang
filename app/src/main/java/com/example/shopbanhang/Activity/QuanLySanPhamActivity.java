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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        imgBack = findViewById(R.id.toolbar);
        imgBack.setOnClickListener(v -> finish());

        floatingActionButton = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.rcy_qlsp);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

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
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham mSanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(mSanPham);
                }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
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
                                            Toast.makeText(QuanLySanPhamActivity.this, "Xóa thành công chi tiết sản phẩm "+chiTietSanPham.getMasp() , Toast.LENGTH_SHORT).show();

                                            myRef2.child(String.valueOf(chiTietSanPham.getIdchitietsanpham())).removeValue();

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
    }



    private void addProducts() {

        Intent intent = new Intent(QuanLySanPhamActivity.this, ThemSanPhamActivity.class);
        startActivity(intent);
    }
}