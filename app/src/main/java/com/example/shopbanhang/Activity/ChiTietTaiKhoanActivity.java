package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.Model.ChiTietTaiKhoan;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChiTietTaiKhoanActivity extends AppCompatActivity {

    private ImageView imgName, imgEmail, imgDiaChi, imgSDT,myImg;
    private TextView txtName, txtEmail, txtDiaChi, txtSDT;
    private Button btnLuu;
    private String user, email;
    private Context context = this;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String IMAGE_URL_KEY = "imageUrlKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_tai_khoan);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = mySharedPreferences.getValue("remember_username_ten");
        email = mySharedPreferences.getValue("remember_username");
        Log.d("HUY", "name :" + user);

        anhxa();

        txtName.setText(user);
        txtEmail.setText(email);

        storageReference = FirebaseStorage.getInstance().getReference();

        myImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LuuThongTin();
            }
        });

        imgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Sửa tên người dùng", txtName, "name");
            }
        });

        imgDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Sửa địa chỉ", txtDiaChi, "address");
            }
        });

        imgSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Sửa số điện thoại", txtSDT, "phoneNumber");
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String imageUrl = prefs.getString(IMAGE_URL_KEY, "");
        if (!imageUrl.isEmpty()) {
            displayImage(imageUrl);
        }

        HienThiThongTin();
    }

    private void anhxa() {
        txtName = findViewById(R.id.txt_ten_chi_tiet);
        txtEmail = findViewById(R.id.txt_email_chi_tiet);
        txtDiaChi = findViewById(R.id.txt_dia_chi);
        txtSDT = findViewById(R.id.txt_sdt);
        imgName = findViewById(R.id.img_tenChiTiet);
        imgEmail = findViewById(R.id.img_emailChiTiet);
        imgDiaChi = findViewById(R.id.img_DiaChi);
        imgSDT = findViewById(R.id.img_SDTChiTiet);
        btnLuu = findViewById(R.id.btnAdd);
        myImg = findViewById(R.id.imageView3);
    }

    private void LuuThongTin() {

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        int idtaikhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TaiKhoan");
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String address = txtDiaChi.getText().toString();
        String phoneNumber = txtSDT.getText().toString();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Dữ liệu trống", Toast.LENGTH_SHORT).show();
            return;
        }
        String stringidtaikhoan = String.valueOf(idtaikhoan);

        Map<String, Object> updates = new HashMap<>();
        updates.put("tentk", name);
        updates.put("emailtk", email);
        updates.put("diachitk", address);
        updates.put("sdttk", phoneNumber);




        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String imageUrl = prefs.getString(IMAGE_URL_KEY, "");
        if (!imageUrl.isEmpty()) {
            updates.put("imgtk", imageUrl);
            mDatabaseReference.child(stringidtaikhoan).updateChildren(updates);
        } else {
            mDatabaseReference.child(stringidtaikhoan).updateChildren(updates);
        }


    }

    private void HienThiThongTin() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        int idtaikhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
        String stringidtaikhoan = String.valueOf(idtaikhoan);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TaiKhoan");
        DatabaseReference userRef = myRef.child(stringidtaikhoan);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);

                    txtName.setText(taiKhoan.getTentk());
                    txtEmail.setText(taiKhoan.getEmailtk());
                    txtDiaChi.setText(taiKhoan.getDiachitk());

                    Log.d("HUY", "Phone Number: " + taiKhoan.getSdttk());

                    txtSDT.setText(taiKhoan.getSdttk());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showEditDialog(String title, TextView textView, String fieldName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        // Hiển thị thông tin hiện tại trong EditText
        input.setText(textView.getText().toString());

        builder.setView(input);

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newValue = input.getText().toString();
                textView.setText(newValue);
//                updateFieldInFirebase(fieldName, newValue);
                LuuThongTin();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateFieldInFirebase(String fieldName, String newValue) {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        int idtaikhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
        String stringidtaikhoan = String.valueOf(idtaikhoan);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TaiKhoan");

        myRef.child(stringidtaikhoan).child(fieldName).setValue(newValue)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    private void uploadImage(Uri imageUri) {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        int idtaikhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
        String stringidtaikhoan = String.valueOf(idtaikhoan);

        StorageReference filePath = storageReference.child("images").child(stringidtaikhoan + ".jpg");

        filePath.putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Hình đã được tải lên thành công", Toast.LENGTH_SHORT).show();

                        // Lấy URL của hình ảnh từ Storage
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateImageInFirebase(imageUrl);

                            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString(IMAGE_URL_KEY, imageUrl);
                            editor.apply();

                        }).addOnFailureListener(e -> {
                            // Xử lý khi không lấy được URL
                            Toast.makeText(this, "Không thể lấy URL của hình ảnh", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(this, "Tải lên hình thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(myImg);
    }

    private void updateImageInFirebase(String imageUrl) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TaiKhoan");
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        int idtaikhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
        String stringidtaikhoan = String.valueOf(idtaikhoan);
        // Cập nhật imageUrl vào Firebase
        myRef.child(stringidtaikhoan).child("imgtk").setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Lưu URL hình thành công", Toast.LENGTH_SHORT).show();

                        displayImage(imageUrl);
                    } else {
                        Toast.makeText(context, "Lưu URL hình thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
