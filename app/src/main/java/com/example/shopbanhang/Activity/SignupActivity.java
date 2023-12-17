package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout hoten,email,matkhau,nhaplaimk;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        button = findViewById(R.id.btnsignup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangKy();
            }
        });
    }



    private void DangKy() {


        button = findViewById(R.id.btnsignup);
        hoten = findViewById(R.id.hotendangky);
        email = findViewById(R.id.emaildangky);
        matkhau = findViewById(R.id.matkhaudangky);
        nhaplaimk = findViewById(R.id.nhaplaimatkhaudangky);







        Random random = new Random();
        String id = String.valueOf(random.nextInt(1000000));
        String hotenkh = hoten.getEditText().getText().toString().trim();
        String emailkh = email.getEditText().getText().toString().trim();
        String matkhaukh = matkhau.getEditText().getText().toString().trim();
        String nhaplaimatkhau = nhaplaimk.getEditText().getText().toString().trim();
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
        }
        else if (!matkhaukh.equals(nhaplaimatkhau)){
            Toast.makeText(SignupActivity.this, "mật khẩu phải giống với nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(emailkh, nhaplaimatkhau)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                TaiKhoan taiKhoan = new TaiKhoan(id,"https://firebasestorage.googleapis.com/v0/b/shopbanhang-38995.appspot.com/o/TaiKhoan%2Fno_image.png?alt=media&token=4a0b719e-6261-44b9-bf56-f509e0775ed2",hotenkh,emailkh,matkhaukh,"none","none","none","none");
                                pushData(taiKhoan);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }



    }

    private void pushData(TaiKhoan taiKhoan) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("TaiKhoan");

        String pathObj = taiKhoan.getIdtk();
        databaseReference.child(pathObj).setValue(taiKhoan);

    }
}