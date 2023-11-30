package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

TextInputEditText hoten,email,matkhau,nhaplaimk;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    private void themKhuyenMai() {


        button = findViewById(R.id.btnsignup);
        hoten = findViewById(R.id.hotendangky);
        email = findViewById(R.id.emaildangky);
        matkhau = findViewById(R.id.matkhaudangky);
        nhaplaimk = findViewById(R.id.nhaplaimatkhaudangky);



        Button btnThemKM = findViewById(R.id.btnThemKM);



        btnThemKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String hotenkh = hoten.getText().toString().trim();
                String emailkh = email.getText().toString().trim();
                String matkhaukh = matkhau.getText().toString().trim();
                String nhaplaimatkhau = nhaplaimk.getText().toString().trim();
                if (hotenkh.equals("")){
                    Toast.makeText(SignupActivity.this, "Chưa nhập tên khách hàng", Toast.LENGTH_SHORT).show();
                    return;
                } else if (emailkh.equals("")){
                    Toast.makeText(SignupActivity.this, "Chưa nhập email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (matkhaukh.equals("")){
                    Toast.makeText(SignupActivity.this, "Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                } else if (nhaplaimatkhau.equals("")){
                    Toast.makeText(SignupActivity.this, "Chưa nhập ngày lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!matkhaukh.equals(nhaplaimatkhau)){
                    Toast.makeText(SignupActivity.this, "mật khẩu phải giống với nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }

            }
        });

    }

    private void pushData(KhuyenMai khuyenMai) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("khachhang");

        String pathObj = khuyenMai.getIdKhuyenMai();
        databaseReference.child(pathObj).setValue(khuyenMai);

    }
}