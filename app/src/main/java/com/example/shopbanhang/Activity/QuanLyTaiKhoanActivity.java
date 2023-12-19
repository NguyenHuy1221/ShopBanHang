package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Adapter.TaiKhoanAdapter;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {
    FloatingActionButton addtk;
    RecyclerView recycle_qltk;
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgtk;
    EditText edtidtk,edtemailtk,edtmktk,edtdctk,edtnttk,edttttk,edtsdttk,edttentk;
    TaiKhoanAdapter taiKhoanAdapter;
    private Uri uri;
    private Context context = this;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage database;
    private List<TaiKhoan> list=new ArrayList<>();
    ImageView backBtntk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);
        initView();
        addtk = findViewById(R.id.addtk);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycle_qltk.setLayoutManager(gridLayoutManager);
        taiKhoanAdapter = new TaiKhoanAdapter(QuanLyTaiKhoanActivity.this,list, new TaiKhoanAdapter.Click(){
            @Override
            public void onUpdateClick(TaiKhoan taiKhoan) {
                suataikhoan(taiKhoan);
            }

            @Override
            public void onDeleteClick(TaiKhoan taiKhoan) {
                xoataikhoan(taiKhoan);
            }
        });
        recycle_qltk.setAdapter(taiKhoanAdapter);



        addtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themtk();
            }
        });

        getlist();
    }

    private void getlist() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("TaiKhoan");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan=snapshot.getValue(TaiKhoan.class);
                if( taiKhoan!= null){
                    list.add(taiKhoan);
                    taiKhoanAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaiKhoan taiKhoan=snapshot.getValue(TaiKhoan.class);
                if(taiKhoan == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i<list.size(); i++){
                    if(taiKhoan.getIdtk() == list.get(i).getIdtk()){
                        list.set(i,taiKhoan);
                        break;
                    }
                }
                taiKhoanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                TaiKhoan taiKhoan=snapshot.getValue(TaiKhoan.class);
                if(taiKhoan == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i<list.size(); i++){
                    if(taiKhoan.getIdtk() == list.get(i).getIdtk()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                taiKhoanAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private List<TaiKhoan> getList() {
//        List<TaiKhoan> list = new ArrayList<>();
//        list.add(new TaiKhoan("1","@drawable/h1.jpg","ngoquoctuan","123"));
//        list.add(new TaiKhoan("2","@drawable/h1.jpg","hoduchau","123"));
//        list.add(new TaiKhoan("3","@drawable/h1.jpg","phanhuythanh","123"));
//        list.add(new TaiKhoan("4","@drawable/h1.jpg","nguyenquanghuy","123"));
//        return list;
//    }

    private void themtk() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyTaiKhoanActivity.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_tai_khoan,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button btnttk=view.findViewById(R.id.btnttk);
        imgtk=view.findViewById(R.id.imgtk);
//        edtidtk=view.findViewById(R.id.edtidtk);
        edtemailtk=view.findViewById(R.id.edtemailtk);
        edtmktk=view.findViewById(R.id.edtmktk);
        edtdctk=view.findViewById(R.id.edtdctk);
        edttentk=view.findViewById(R.id.edttentk);
        edtsdttk=view.findViewById(R.id.edtsdttk);
        edttttk=view.findViewById(R.id.edttttk);
        edtnttk=view.findViewById(R.id.edtnttk);
        imgtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenFile();
            }
        });


        btnttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtemailtk.getText().toString().trim().equals("") ||
                        edtmktk.getText().toString().trim().equals("") ||
                        edttentk.getText().toString().trim().equals("")||
                        edtdctk.getText().toString().trim().equals("")||
                        edtsdttk.getText().toString().trim().equals("")||
                        edttttk.getText().toString().trim().equals("")||
                        edtnttk.getText().toString().trim().equals("")){
                    Toast.makeText(QuanLyTaiKhoanActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//                } else if (uri==null) {
//                    Toast.makeText(context, "Chưa cho hình ảnh", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(QuanLyTaiKhoanActivity.this, "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();
                    StorageReference storage = FirebaseStorage.getInstance().getReference().child("taikhoan").child(uri.getLastPathSegment());

                    storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlimg = uriTask.getResult();
                            String img = urlimg.toString();
                            Random random = new Random();
                            String id = String.valueOf(random.nextInt(1000000));
                            String email = edtemailtk.getText().toString().trim();
                            String mk = edtmktk.getText().toString().trim();
                            String ten = edttentk.getText().toString().trim();
                            String dc = edtdctk.getText().toString().trim();
                            String tt = edttttk.getText().toString().trim();
                            String nt = edtnttk.getText().toString().trim();
                            String sdt = edtsdttk.getText().toString().trim();
                            TaiKhoan taiKhoan = new TaiKhoan(id,img,ten,email,mk,sdt,dc,nt,tt);
                            pushData(taiKhoan);
                            dialog.dismiss();
                        }
                    });

                }
            }
        });
    }

    private void suataikhoan(TaiKhoan taiKhoan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyTaiKhoanActivity.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_tai_khoan,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnttk=view.findViewById(R.id.btnttk);
        imgtk=view.findViewById(R.id.imgtk);
//        edtidtk=view.findViewById(R.id.edtidtk);
        edtemailtk=view.findViewById(R.id.edtemailtk);
        edtmktk=view.findViewById(R.id.edtmktk);
        edtdctk=view.findViewById(R.id.edtdctk);
        edttentk=view.findViewById(R.id.edttentk);
        edtsdttk=view.findViewById(R.id.edtsdttk);
        edttttk=view.findViewById(R.id.edttttk);
        edtnttk=view.findViewById(R.id.edtnttk);
        btnttk.setText("Sửa Tài Khoản");

        Picasso.get().load(taiKhoan.getImgtk()).into(imgtk);
        edtemailtk.setText(taiKhoan.getEmailtk());
        edtmktk.setText(taiKhoan.getMatkhautk());
        edtdctk.setText(taiKhoan.getDiachitk());
        edttentk.setText(taiKhoan.getTentk());
        edtsdttk.setText(taiKhoan.getSdttk());
        edttttk.setText(taiKhoan.getTinhtragtk());
        edtnttk.setText(taiKhoan.getNgaytaotk());

        imgtk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenFile();
            }
        });

        btnttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference("TaiKhoan");
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("TaiKhoan").child(uri.getLastPathSegment());

                String emailtkMoi = edtemailtk.getText().toString().trim();
                String tentkMoi = edttentk.getText().toString().trim();
                String mktkmoi = edtmktk.getText().toString().trim();
                String sdttkmoi = edtsdttk.getText().toString().trim();
                String dctkmoi = edtdctk.getText().toString().trim();
                String nttkmoi = edtnttk.getText().toString().trim();
                String tttkmoi = edttttk.getText().toString().trim();



                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uriImg = uriTask.getResult();
                        String img = uriImg.toString();
                        taiKhoan.setImgtk(img);
                        taiKhoan.setEmailtk(emailtkMoi);
                        taiKhoan.setTentk(tentkMoi);
                        taiKhoan.setMatkhautk(mktkmoi);
                        taiKhoan.setDiachitk(dctkmoi);
                        taiKhoan.setSdttk(sdttkmoi);
                        taiKhoan.setNgaytaotk(nttkmoi);
                        taiKhoan.setTinhtragtk(tttkmoi);
                        databaseReference.child(taiKhoan.getIdtk()).updateChildren(taiKhoan.toMap());
                        dialog.dismiss();
                    }
                });







            }
        });

    }

    private void xoataikhoan(TaiKhoan taiKhoan) {
        new AlertDialog.Builder(this)
                .setTitle("Shop Quần Áo")
                .setMessage("Xác Nhận Xóa ??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("TaiKhoan");

                        myRef.child(String.valueOf(taiKhoan.getIdtk())).removeValue();

                    }
                })

                .setNegativeButton("Cancel",null)
                .show();
    }

    private void pushData(TaiKhoan taiKhoan) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("TaiKhoan");
        String pathObj = String.valueOf(taiKhoan.getIdtk());
        databaseReference.child(pathObj).setValue(taiKhoan);
    }

    private void initView() {
        addtk=findViewById(R.id.addtk);
        recycle_qltk = findViewById(R.id.recycle_qltk);
        backBtntk = findViewById(R.id.backBtntk);
//        backBtntk.setOnClickListener(v -> finish());
        backBtntk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuanLyTaiKhoanActivity.this,Trang_Ca_Nhan.class);
                startActivity(intent);
            }
        });
    }
    private void oppenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(imgtk);
        }
    }
}