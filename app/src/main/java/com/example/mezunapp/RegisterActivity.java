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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    TextView editTextEmail, editTextPassword, editTextPassword2, loginNow;
    Button buttonReg;
    FirebaseUser user;
    private FirebaseAuth auth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mezunapp-bc960-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPassword2 = findViewById(R.id.password2);
        buttonReg = findViewById(R.id.signupbutton2);
        loginNow = findViewById(R.id.loginNow);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getWindow().setBackgroundDrawableResource(R.drawable.bg);

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userIdTxt, emailTxt, nameTxt, surnameTxt, phoneTxt, departmentTxt, entranceYearTxt, accomStateTxt, durationTxt, maxDistanceTxt, passwordTxt, password2Txt;
                emailTxt = String.valueOf(editTextEmail.getText());
                passwordTxt = String.valueOf(editTextPassword.getText());
                password2Txt = String.valueOf(editTextPassword2.getText());
                userIdTxt = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                nameTxt = "";
                surnameTxt = "";
                phoneTxt = "";
                departmentTxt = "";
                entranceYearTxt = "";
                accomStateTxt = "";
                durationTxt = "";
                maxDistanceTxt = "0.0";
                if(TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(passwordTxt)){
                    Toast.makeText(RegisterActivity.this, "Lütfen bütün alanları doldurun", Toast.LENGTH_SHORT).show();
                /*}else if (!(emailTxt.contains("std.yildiz.edu.tr"))) {
                    Toast.makeText(RegisterActivity.this, "Enter valid YTU email", Toast.LENGTH_SHORT).show();
                */
                }else if(passwordTxt.equals(password2Txt)){
                    auth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            writeNewUser(userIdTxt,emailTxt, nameTxt, surnameTxt, phoneTxt, departmentTxt, entranceYearTxt, accomStateTxt, durationTxt, maxDistanceTxt);
                                            Toast.makeText(RegisterActivity.this, "Hesap oluşturuldu. Onay maili için gelen kutunuzu kontrol edin.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Şifreler aynı olmak zorunda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void writeNewUser(String user_id, String email, String first_name, String last_name, String phone, String department, String entrance, String state, String duration, String distance) {
        databaseReference.child("users").child(user_id).child("Email").setValue(email);
        databaseReference.child("users").child(user_id).child("Name").setValue(first_name);
        databaseReference.child("users").child(user_id).child("Surname").setValue(last_name);
        databaseReference.child("users").child(user_id).child("Phone").setValue(phone);
        databaseReference.child("users").child(user_id).child("Department").setValue(department);
        databaseReference.child("users").child(user_id).child("Entrance").setValue(entrance);
        databaseReference.child("users").child(user_id).child("State").setValue(state);
        databaseReference.child("users").child(user_id).child("Duration").setValue(duration);
        databaseReference.child("users").child(user_id).child("MaxDistance").setValue(distance);
    }
}