package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.UUID;

public class quan_ly_khuyen_mai extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private FloatingActionButton floatAdd;
    private KhuyenMaiAdapter khuyenMaiAdapter;
    private RecyclerView recycle_qlkm;
    private ImageView backBtnKM, imgKM;
    private Uri uri;
    private List<KhuyenMai> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khuyen_mai);
        initView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycle_qlkm.setLayoutManager(gridLayoutManager);
        khuyenMaiAdapter = new KhuyenMaiAdapter(quan_ly_khuyen_mai.this, list, new KhuyenMaiAdapter.Click() {
            @Override
            public void onUpdateClick(KhuyenMai khuyenMai) {
                suaKhuyenMai(khuyenMai);
            }

            @Override
            public void onDeleteClick(KhuyenMai khuyenMai) {
                deleteKhuyenMai(khuyenMai);
            }
        });
        recycle_qlkm.setAdapter(khuyenMaiAdapter);

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themKhuyenMai();
            }
        });

        getList();
    }




    private void themKhuyenMai() {
        AlertDialog.Builder builder = new AlertDialog.Builder(quan_ly_khuyen_mai.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_khuyen_mai,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        imgKM = view.findViewById(R.id.imgAddKM);
        TextView txtNameAddKM = view.findViewById(R.id.txtNameAddKM);
        TextView txtPhanTramAddKM = view.findViewById(R.id.txtPhanTramAddKM);
        TextView txtNgayBatDauAddKM = view.findViewById(R.id.txtNgayBatDauAddKM);
        TextView txtNgayKetThucAddKM = view.findViewById(R.id.txtNgayKetThucAddKM);
        Button btnThemKM = view.findViewById(R.id.btnThemKM);

        imgKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        btnThemKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String tenKM = txtNameAddKM.getText().toString().trim();
                String phanTramKM = txtPhanTramAddKM.getText().toString().trim();
                String ngayBatDau = txtNgayBatDauAddKM.getText().toString().trim();
                String ngayKetThuc = txtNgayKetThucAddKM.getText().toString().trim();
                if (tenKM.equals("")){
                    Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập tên khuyến mãi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phanTramKM.equals("")){
                    Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập phần trăm khuyến mãi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ngayBatDau.equals("")){
                    Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập ngày bắt đầu khuyến mãi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ngayKetThuc.equals("")){
                    Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập ngày kết thúc khuyến mãi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (uri == null) {
                    Toast.makeText(quan_ly_khuyen_mai.this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("KhuyenMai").child(uri.getLastPathSegment());
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri uriImg = uriTask.getResult();
                            String img = uriImg.toString();
                            KhuyenMai khuyenMai = new KhuyenMai(id,img,tenKM,ngayBatDau,ngayKetThuc,phanTramKM);
                            pushData(khuyenMai);
                            dialog.dismiss();
                        }
                    });
                }

            }
        });

    }

    private void suaKhuyenMai(KhuyenMai khuyenMai) {
        AlertDialog.Builder builder = new AlertDialog.Builder(quan_ly_khuyen_mai.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_khuyen_mai,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        imgKM = view.findViewById(R.id.imgAddKM);
        TextView txtEditTenKM = view.findViewById(R.id.txtNameAddKM);
        TextView txtEditPhanTramKM = view.findViewById(R.id.txtPhanTramAddKM);
        TextView txtEditNgayBatDauKM = view.findViewById(R.id.txtNgayBatDauAddKM);
        TextView txtEditNgayKetThucKM = view.findViewById(R.id.txtNgayKetThucAddKM);
        Button btnThemKM = view.findViewById(R.id.btnThemKM);
        btnThemKM.setText("Sửa Khuyến Mãi");

        Picasso.get().load(khuyenMai.getImgKhuyenMai()).into(imgKM);
        txtEditTenKM.setText(khuyenMai.getTenKhuyenMai());
        txtEditPhanTramKM.setText(khuyenMai.getPhanTramKhuyenMai());
        txtEditNgayBatDauKM.setText(khuyenMai.getNgayBatDau());
        txtEditNgayKetThucKM.setText(khuyenMai.getNgayKetThuc());

        imgKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        btnThemKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("KhuyenMai");
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("KhuyenMai").child(uri.getLastPathSegment());

                String tenKhuyenMaiMoi = txtEditTenKM.getText().toString().trim();
                String phanTramKhuyenMaiMoi = txtEditPhanTramKM.getText().toString().trim();
                String ngayBatDauKhuyenMaiMoi = txtEditNgayBatDauKM.getText().toString().trim();
                String ngayKetThucKhuyenMaiMoi = txtEditNgayKetThucKM.getText().toString().trim();



                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uriImg = uriTask.getResult();
                        String img = uriImg.toString();
                        khuyenMai.setImgKhuyenMai(img);
                        khuyenMai.setTenKhuyenMai(tenKhuyenMaiMoi);
                        khuyenMai.setPhanTramKhuyenMai(phanTramKhuyenMaiMoi);
                        khuyenMai.setNgayBatDau(ngayBatDauKhuyenMaiMoi);
                        khuyenMai.setNgayKetThuc(ngayKetThucKhuyenMaiMoi);
                        databaseReference.child(khuyenMai.getIdKhuyenMai()).updateChildren(khuyenMai.toMap());
                        dialog.dismiss();
                    }
                });







            }
        });

    }

    private void deleteKhuyenMai(KhuyenMai khuyenMai) {
        new AlertDialog.Builder(this)
                .setTitle("Shop Quần Áo")
                .setMessage("Xác Nhận Xóa ??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("KhuyenMai");

                        myRef.child(String.valueOf(khuyenMai.getIdKhuyenMai())).removeValue();

                    }
                })

                .setNegativeButton("Cancel",null)
                .show();
    }
    private void pushData(KhuyenMai khuyenMai) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("KhuyenMai");

        String pathObj = khuyenMai.getIdKhuyenMai();
        databaseReference.child(pathObj).setValue(khuyenMai);

    }


    private void getList(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("KhuyenMai");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                if(khuyenMai != null){
                    list.add(khuyenMai);
                    khuyenMaiAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                if(khuyenMai == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i<list.size(); i++){
                    if(khuyenMai.getIdKhuyenMai() == list.get(i).getIdKhuyenMai()){
                        list.set(i,khuyenMai);
                        break;
                    }
                }
                    khuyenMaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                if(khuyenMai == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i<list.size(); i++){
                    if(khuyenMai.getIdKhuyenMai() == list.get(i).getIdKhuyenMai()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                khuyenMaiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            Picasso.get().load(uri).into(imgKM);
        }
    }

    private void initView() {
        floatAdd = findViewById(R.id.floatAdd);
        recycle_qlkm = findViewById(R.id.recycle_qlkm);
        backBtnKM = findViewById(R.id.backBtnKM);
        backBtnKM.setOnClickListener(v -> finish());

    }


}