package com.example.shopbanhang.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ThuongHieuActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private Uri mImageUri;
    private Context context = this;
    ImageView imgBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuong_hieu);

        floatingActionButton = findViewById(R.id.fabAddBrand);
        recyclerView = findViewById(R.id.recyclerViewBrands);

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
                String tenTH = edtName.getText().toString().trim();
                ThuongHieu thuongHieu = new ThuongHieu();
                thuongHieu.setTenThuongHieu(tenTH);
                // Nếu có ảnh, tải ảnh lên Firebase Storage
                if (mImageUri != null) {
                    uploadImageToFirebaseStorage(mImageUri, thuongHieu);
                } else {
                    // Nếu không có ảnh, lưu thông tin thương hiệu vào Realtime Database
                    saveBrandToDatabase(thuongHieu, "");
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imgBrand);
        }
    }



    private void oppenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, ThuongHieu thuongHieu) {
        // Tạo thư mục trong Firebase Storage để lưu trữ ảnh
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("brands");
        final StorageReference imageRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        // Tải ảnh lên Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Lấy URL của ảnh sau khi tải lên thành công
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                // Lưu thông tin thương hiệu và URL ảnh vào Realtime Database
                                saveBrandToDatabase(thuongHieu, imageUrl);
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

    private void saveBrandToDatabase(ThuongHieu thuongHieu, String imageUrl) {
        // Lưu thông tin thương hiệu vào Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("brands");
        String brandId = databaseRef.push().getKey();
        thuongHieu.setIdThuongHieu(brandId);
        thuongHieu.setImageUrl(imageUrl);

        databaseRef.child(brandId).setValue(thuongHieu)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xử lý khi lưu dữ liệu thành công
                        Toast.makeText(context, "Thêm thương hiệu thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi lưu dữ liệu thất bại
                        Toast.makeText(context, "Thêm thương hiệu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}