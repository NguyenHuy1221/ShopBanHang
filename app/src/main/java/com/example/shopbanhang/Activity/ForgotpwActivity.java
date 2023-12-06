package com.example.shopbanhang.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpwActivity extends AppCompatActivity {

    private TextInputLayout edtForgot;
    private Button btnForgot;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);
        edtForgot = findViewById(R.id.edtForgot);
        btnForgot = findViewById(R.id.btnForgot);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtForgot.getEditText().getText().toString().trim();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                String emailAddress = email;

                if(emailAddress.equals("")){
                    Toast.makeText(ForgotpwActivity.this, "Chưa nhập email", Toast.LENGTH_SHORT).show();
                }

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotpwActivity.this, "Send Email", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotpwActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                Toast.makeText(ForgotpwActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}