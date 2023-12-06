package com.example.shopbanhang.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private List<TaiKhoan> list = new ArrayList<>();
    Button btnlogin;
    TextInputLayout edtemaillg,edtmklg;
    TextView txtdk, txtquenmk;
    private TaiKhoan taiKhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(LoginActivity.this,""+ list.size(), Toast.LENGTH_SHORT).show();
        Update();
        anhxa();
        next();
        txtdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
               startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, TrangChuActivity.class);
//                startActivity(intent);
                String email = edtemaillg.getEditText().getText().toString().trim();
                String pass = edtmklg.getEditText().getText().toString().trim();
                if (email.equals("")) {
                    edtemaillg.setError("Không để trống");
                    return;
                } else {
                    edtemaillg.setError(null);
                }
                if (pass.equals("")) {
                    edtmklg.setError("Không để trống");
                    return;
                } else {
                    edtmklg.setError(null);
                }
                if (edtemaillg.getEditText().getText().toString().trim().equals("admin") && edtmklg.getEditText().getText().toString().trim().equals("123")) {
                    Intent intent = new Intent(LoginActivity.this, Trang_Ca_Nhan.class);
                    startActivity(intent);
                }else {
                    signUp();
                }

            }
        });

        txtquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotpwActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        String email = edtemaillg.getEditText().getText().toString().trim();
        String pass = edtmklg.getEditText().getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("TaiKhoan");

                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getEmailtk().equals(edtemaillg.getEditText().getText().toString())) {
                                    databaseReference.child(list.get(i).getIdtk()).child("matkhautk").setValue(edtmklg.getEditText().getText().toString());
                                }
                            }

                            Intent intent = new Intent(LoginActivity.this, TrangChuActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void Update() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("TaiKhoan");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    taiKhoan = dataSnapshot.getValue(TaiKhoan.class);
                    list.add(taiKhoan);

                }


            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    private void next() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

        } else {
            Intent intent = new Intent(this,TrangChuActivity.class);
            startActivity(intent);
        }
    }
    private void anhxa() {
        edtemaillg=findViewById(R.id.edtemaillg);
        edtmklg=findViewById(R.id.edtmklg);
        btnlogin=findViewById(R.id.btnlogin);
        txtdk=findViewById(R.id.txtdk);
        txtquenmk=findViewById(R.id.txtquenmk);

    }
}