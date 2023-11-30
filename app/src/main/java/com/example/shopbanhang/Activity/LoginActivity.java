package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    Button btnlogin;
    TextInputLayout edtemaillg,edtmklg;
    TextView txtdk, txtquenmk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
//                Intent intent = new Intent(LoginActivity.this, ForgotpwActivity.class);
//                startActivity(intent);
                String email = edtemaillg.getEditText().getText().toString().trim();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = email;

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Send Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
                            Intent intent = new Intent(LoginActivity.this, TrangChuActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
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