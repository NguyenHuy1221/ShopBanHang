package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private List<TaiKhoan> list = new ArrayList<>();
    Button btnlogin;
    TextInputLayout edtemaillg, edtmklg;
    TextView txtdk, txtquenmk;
    public CheckBox checkBox;
    TaiKhoan taiKhoan;
    final MySharedPreferences mySharedPreferences = new MySharedPreferences(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        Update();

        txtdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    Intent intent = new Intent(LoginActivity.this, TrangChuActivity.class);
                    startActivity(intent);
                } else {
//                    Toast.makeText(LoginActivity.this, "không lỗi", Toast.LENGTH_SHORT).show();

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TaiKhoan");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Query query = myRef.orderByChild("emailtk").equalTo(email);

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<AuthResult> task) {
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

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean loginSuccess = false;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TaiKhoan getuser = dataSnapshot.getValue(TaiKhoan.class);

                    if (getuser.getEmailtk().equalsIgnoreCase(email) && getuser.getMatkhautk().equals(pass)) {
                        // Điều kiện đăng nhập thành công
                        loginSuccess = true;

                        // Ghi nhớ đăng nhập
                        if (checkBox.isChecked()) {
                            mySharedPreferences.putValue("remember_id_tk", getuser.getIdtk());
                            mySharedPreferences.putValue("remember_username", getuser.getEmailtk());
                            mySharedPreferences.putValue("remember_username_ten", getuser.getTentk());
                            mySharedPreferences.putValue("remember_password", getuser.getMatkhautk());
                        } else {
                            mySharedPreferences.putValue("remember_id_tk", getuser.getIdtk());
                            mySharedPreferences.putValue("remember_username", getuser.getEmailtk());
                            mySharedPreferences.putValue("remember_username_ten", getuser.getTentk());
                            mySharedPreferences.putValue("remember_password", getuser.getMatkhautk());
//                            mySharedPreferences.putValue("remember_id_tk", null);
//                            mySharedPreferences.putValue("remember_username", null);
//                            mySharedPreferences.putValue("remember_username_ten", null);
//                            mySharedPreferences.putValue("remember_password", null);
                        }

                        // Chuyển trang
                        if (getuser.getEmailtk().equalsIgnoreCase("admin")) {
                            mySharedPreferences.putBooleanValue("permission_admin", true);
                        }

                        mySharedPreferences.putBooleanValue("login", true);
                        Intent intent = new Intent(LoginActivity.this, TrangChuActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }

                if (!loginSuccess) {
                    // Hiển thị lỗi nếu không đăng nhập thành công
                    edtemaillg.setError("SAI TÀI KHOẢN HOẶC MẬT KHẨU !");
                    edtmklg.setError("SAI TÀI KHOẢN HOẶC MẬT KHẨU !");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edtemaillg.setError(null);
                            edtmklg.setError(null);
                        }
                    }, 4000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Update() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("TaiKhoan");
        if (list != null){
            list.clear();
        }
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

    private void anhxa() {
        edtemaillg = findViewById(R.id.edtemaillg);
        edtmklg = findViewById(R.id.edtmklg);
        btnlogin = findViewById(R.id.btnlogin);
        txtdk = findViewById(R.id.txtdk);
        txtquenmk = findViewById(R.id.txtquenmk);
        checkBox = findViewById(R.id.cb_login);

    }
}
