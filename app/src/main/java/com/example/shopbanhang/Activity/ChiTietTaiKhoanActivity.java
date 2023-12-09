package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.ChiTietTaiKhoan;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChiTietTaiKhoanActivity extends AppCompatActivity {

    private ImageView imgName,imgEmail,imgDiaChi,imgSDT,imgPass;
    private TextView txtName,txtEmail,txtDiaChi,txtSDT,txtPass;
    private Button btnLuu;
    private String user,email;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_tai_khoan);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = mySharedPreferences.getValue("remember_username_ten");
        email = mySharedPreferences.getValue("remember_username");
        Log.d("HUY","name :" + user);



        anhxa();

        txtName.setText(user);
        txtEmail.setText(email);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LuuThongTin();
            }
        });

        imgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNameDialog();
            }
        });

//        imgEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEditAddressDialog();
//            }
//        });

        imgDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditAddressDialog();
            }
        });

        imgSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPhoneDialog();
            }
        });

        HienThiThongTin();

    }

    private void anhxa() {
        txtName = findViewById(R.id.txt_ten_chi_tiet);
        txtEmail = findViewById(R.id.txt_email_chi_tiet);
        txtDiaChi = findViewById(R.id.txt_dia_chi);
        txtSDT = findViewById(R.id.txt_sdt);
        txtPass = findViewById(R.id.txt_pass);
        imgName = findViewById(R.id.img_tenChiTiet);
        imgEmail = findViewById(R.id.img_emailChiTiet);
        imgDiaChi = findViewById(R.id.img_DiaChi);
        imgSDT = findViewById(R.id.img_SDTChiTiet);
        imgPass = findViewById(R.id.img_MKChiTiet);
        btnLuu = findViewById(R.id.btnAdd);
    }

    private void LuuThongTin() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chiTietTaiKhoan");

        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Dữ liệu trống", Toast.LENGTH_SHORT).show();
            return;
        }

        ChiTietTaiKhoan chiTietTaiKhoan = new ChiTietTaiKhoan();
        chiTietTaiKhoan.setName(name);
        chiTietTaiKhoan.setEmail(email);

        myRef.child(email.replace(".", "_")).setValue(chiTietTaiKhoan)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Lưu Thông tin thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Lưu Thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void HienThiThongTin() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chiTietTaiKhoan");
        DatabaseReference userRef = myRef.child(email.replace(".","_"));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ChiTietTaiKhoan chiTietTaiKhoan = snapshot.getValue(ChiTietTaiKhoan.class);

                    txtName.setText(chiTietTaiKhoan.getName());
                    txtEmail.setText(chiTietTaiKhoan.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ChiTietTaiKhoanActivity", "Failed to read value.", error.toException());
            }
        });

    }



    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa tên người dùng");

        final EditText input = new EditText(this);

        input.setText(txtName.getText().toString());

        builder.setView(input);

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                
                txtName.setText(newName);
                updateNameInFirebase(newName);
            }
        });

        // Thiết lập nút Negative (hủy)
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateNameInFirebase(String newName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chiTietTaiKhoan");

        // Cập nhật tên mới vào Firebase
        myRef.child(email.replace(".", "_")).child("name").setValue(newName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lưu thát bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showEditPhoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa số điện thoại");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        // Hiển thị số điện thoại hiện tại trong EditText
        input.setText(txtSDT.getText().toString());

        builder.setView(input);

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPhone = input.getText().toString();
                txtSDT.setText(newPhone);
                updatePhoneInFirebase(newPhone);
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

    private void updatePhoneInFirebase(String newPhone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chiTietTaiKhoan");

        myRef.child(email.replace(".", "_")).child("phone").setValue(newPhone)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Lưu số điện thoại thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lưu số điện thoại thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showEditAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa địa chỉ");

        final EditText input = new EditText(this);
        // Hiển thị địa chỉ hiện tại trong EditText
        input.setText(txtDiaChi.getText().toString());

        builder.setView(input);

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAddress = input.getText().toString();
                txtDiaChi.setText(newAddress);
                updateAddressInFirebase(newAddress);
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

    private void updateAddressInFirebase(String newAddress) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chiTietTaiKhoan");

        myRef.child(email.replace(".", "_")).child("address").setValue(newAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Lưu địa chỉ thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lưu địa chỉ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}