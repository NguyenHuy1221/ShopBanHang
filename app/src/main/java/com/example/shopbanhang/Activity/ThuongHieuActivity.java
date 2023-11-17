package com.example.shopbanhang.Activity;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Adapter.ThuongHieuAdapter;
import com.example.shopbanhang.DAO.ThuongHieuDAO;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ThuongHieuActivity extends AppCompatActivity  {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private ThuongHieuAdapter thuongHieuAdapter;
    private FloatingActionButton floatingActionButton;
    private Uri mImageUri;
    private Context context = this;
    private ImageView imgBrand;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage database;

    private List<ThuongHieu> mThuongHieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuong_hieu);

        floatingActionButton = findViewById(R.id.fabAddBrand);
        recyclerView = findViewById(R.id.recyclerViewBrands);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mThuongHieu = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("thuonghieu");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mThuongHieu.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    ThuongHieu thuongHieu = postSnapshot.getValue(ThuongHieu.class);
                    mThuongHieu.add(thuongHieu);
                }
                thuongHieuAdapter = new ThuongHieuAdapter(context, mThuongHieu, new ThuongHieuAdapter.IclickListener() {
                    @Override
                    public void onClickUpdateItem(ThuongHieu thuongHieu) {
                        EditThuongHieu(thuongHieu);
                    }

                    @Override
                    public void onClickDeleteItem(ThuongHieu thuongHieu) {
                        DeleteThuongHieu(thuongHieu);
                    }
                });
                recyclerView.setAdapter(thuongHieuAdapter);
                thuongHieuAdapter.updateList(mThuongHieu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBrands();
            }
        });
    }


    private void addBrands() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_thuong_hieu,null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        imgBrand = view.findViewById(R.id.imgBrand);
        EditText edtName = view.findViewById(R.id.edtBrandName);
        Button btnThem = view.findViewById(R.id.btnAddThuongHieu);

        imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenFile();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenTH = edtName.getText().toString().trim().toUpperCase();
                String id = UUID.randomUUID().toString();
                ThuongHieu thuongHieu = new ThuongHieu();
                thuongHieu.setTenThuongHieu(tenTH);
                // Nếu có ảnh, tải ảnh lên Firebase Storage
                if (tenTH.equals("")){
                    Toast.makeText(context, "Chưa nhập tên thương hiệu", Toast.LENGTH_SHORT).show();
                }else if (mImageUri == null) {
                    Toast.makeText(context, "Chưa chọn ảnh ", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImageToFirebaseStorage(mImageUri, id,tenTH);
                    dialog.dismiss();
                }
            }
        });

    }

    // mở ảnh thư viện máy
    private void oppenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri,String id,String name ) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://realtimedata-1e0aa.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".PNG");

        // Tải ảnh lên Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Lấy URL của ảnh sau khi tải lên thành công
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();
                                thuongHieuDAO.insertThuongHieu(new ThuongHieu(id,name,uri.toString()));
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi tải ảnh lên thất bại
                        Toast.makeText(context, "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void EditThuongHieu(ThuongHieu thuongHieu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_thuong_hieu, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        imgBrand = view.findViewById(R.id.imgBrand);
        TextView tenth = view.findViewById(R.id.update_th);
        EditText edtName = view.findViewById(R.id.edtBrandName);
        Button btnSua = view.findViewById(R.id.btnAddThuongHieu);

        tenth.setText("Sửa thương hiệu");
        btnSua.setText("Sửa");
        edtName.setText(thuongHieu.getTenThuongHieu());

        if (imgBrand != null) {
            Picasso.get().load(thuongHieu.getImageUrl()).into(imgBrand);
        }

        imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenFile();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suaTen = edtName.getText().toString().trim().toUpperCase();
                Picasso.get().load(thuongHieu.getImageUrl()).into(imgBrand);

                if (suaTen.equals("")) {
                    Toast.makeText(context, "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    updateThuongHieu(thuongHieu,suaTen);
                    dialog.dismiss();
                    Toast.makeText(context, "Sửa thương hiệu thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateThuongHieu(ThuongHieu thuongHieu,String ten) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://realtimedata-1e0aa.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".PNG");

        imageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        thuongHieu.setTenThuongHieu(ten);
                        thuongHieu.setImageUrl(uri.toString());
                        ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();
                        thuongHieuDAO.updateThuongHieu(thuongHieu);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void DeleteThuongHieu(ThuongHieu thuongHieu){
        new AlertDialog.Builder(context)
                .setTitle("Xóa thương hiệu")
                .setMessage("Bạn có chắc muốn xóa thương hiệu này")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();
                        thuongHieuDAO.deleteThuongHieu(thuongHieu);
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Không", null).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imgBrand);
        }
    }

}