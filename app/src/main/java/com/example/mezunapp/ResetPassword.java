package com.example.mezunapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    TextView editTextEmail;
    Button buttonReset, buttonBack;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();
        buttonReset = findViewById(R.id.buttonReset);
        buttonBack = findViewById(R.id.buttonBack);
        editTextEmail = findViewById(R.id.emailReset);
        user = auth.getCurrentUser();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(editTextEmail.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ResetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }else{
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Şifre yenileme bağlantııs gönderildi", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ResetPassword.this, "HATA", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
