package com.example.mezunapp;


import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    EditText editTextEmail, editTextPhone, editTextName, editTextSurname, editTextDepartment;
    TextView editPP, sliderResult;
    Button btnSave, btnDelete1, btnDelete2, btnDelete3, btnDelete4, btnDelete5, btnDelete6, btnMain;
    Slider slider;
    Spinner spinner1, spinner2, spinner3;
    ImageView uploadImg;
    Uri photoUrl;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mezunapp-bc960-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageRef;
    float maxDistance;
    private static final int REQUEST_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        editTextName = findViewById(R.id.name);
        editTextSurname = findViewById(R.id.surname);
        editTextEmail = findViewById(R.id.email);
        editTextPhone = findViewById(R.id.phone);
        editTextDepartment = findViewById(R.id.department);
        editPP = findViewById(R.id.editpp);
        btnSave = findViewById(R.id.savebutton);
        btnDelete1 = findViewById(R.id.deleteButton1);
        btnDelete2 = findViewById(R.id.deleteButton2);
        btnDelete3 = findViewById(R.id.deleteButton3);
        btnDelete4 = findViewById(R.id.deleteButton4);
        btnDelete5 = findViewById(R.id.deleteButton5);
        btnDelete6 = findViewById(R.id.deleteButton6);
        btnMain = findViewById(R.id.mainbutton);
        uploadImg = findViewById(R.id.photo);
        slider = findViewById(R.id.slider);
        sliderResult = findViewById(R.id.sliderResult);
        maxDistance = 0.0f;
        spinner1 = (Spinner) findViewById(R.id.spinnerYear);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.years_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner2 = (Spinner) findViewById(R.id.spinnerState);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner3 = (Spinner) findViewById(R.id.spinnerTime);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.dates_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        user = auth.getCurrentUser();
        getWindow().setBackgroundDrawableResource(R.drawable.bg);


        if (user != null) {
            photoUrl = user.getPhotoUrl();
            readFromDB(user.getUid());
        }else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                maxDistance = value;
                String result = value + " km";
                sliderResult.setText(result);
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRef = databaseReference.child("users").child(user.getUid()).child("Phone");
                childRef.setValue("")
                        .addOnSuccessListener(aVoid -> {
                            editTextPhone.setText("");
                            Toast.makeText(UserProfile.this, "Telefon başarıyla silindi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserProfile.this, "Telefon silinirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRef = databaseReference.child("users").child(user.getUid()).child("Department");
                childRef.setValue("")
                        .addOnSuccessListener(aVoid -> {
                            editTextDepartment.setText("");
                            Toast.makeText(UserProfile.this, "Fakülte / bölüm bilgileri başarıyla silindi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserProfile.this, "Fakülte / bölüm bilgileri silinirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRef = databaseReference.child("users").child(user.getUid()).child("Entrance");
                childRef.setValue("")
                        .addOnSuccessListener(aVoid -> {
                            spinner1.setSelection(0);
                            Toast.makeText(UserProfile.this, "Giriş yılı başarıyla silindi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserProfile.this, "Giriş yılı silinirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRef = databaseReference.child("users").child(user.getUid()).child("State");
                childRef.setValue("")
                        .addOnSuccessListener(aVoid -> {
                            spinner2.setSelection(0);
                            Toast.makeText(UserProfile.this, "Arama durumu başarıyla silindi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserProfile.this, "Arama durumu silinirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRef = databaseReference.child("users").child(user.getUid()).child("MaxDistance");
                childRef.setValue("0.0")
                        .addOnSuccessListener(aVoid -> {
                            slider.setValue(0.0f);
                            sliderResult.setText("0.0 km");
                            Toast.makeText(UserProfile.this, "Uzaklık bilgisi başarıyla silindi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserProfile.this, "Uzaklık bilgisi silinirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference childRef = databaseReference.child("users").child(user.getUid()).child("Duration");
                childRef.setValue("")
                        .addOnSuccessListener(aVoid -> {
                            spinner3.setSelection(0);
                            Toast.makeText(UserProfile.this, "Kalma süresi başarıyla silindi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UserProfile.this, "Kalma süresi silinirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid, nameTxt, surnameTxt, emailTxt, phoneTxt, departmentTxt, entranceYearTxt, accomStateTxt, stayTimeTxt, maxDistanceTxt;
                uid = user.getUid();
                nameTxt = String.valueOf(editTextName.getText());
                surnameTxt = String.valueOf(editTextSurname.getText());
                emailTxt = String.valueOf(editTextEmail.getText());
                phoneTxt = String.valueOf(editTextPhone.getText());
                departmentTxt = String.valueOf(editTextDepartment.getText());
                String value = spinner1.getSelectedItem().toString();
                if(value.equals("Seçin..")){
                    entranceYearTxt="";
                }else{
                    entranceYearTxt=value;
                }
                value = spinner2.getSelectedItem().toString();
                if(value.equals("Seçin..")){
                    accomStateTxt="";
                }else{
                    accomStateTxt=value;
                }
                value = spinner3.getSelectedItem().toString();
                if(value.equals("Seçin..")){
                    stayTimeTxt="";
                }else{
                    stayTimeTxt=value;
                }
                maxDistanceTxt = String.valueOf(maxDistance);
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        updateUser(uid, emailTxt, nameTxt, surnameTxt, phoneTxt, departmentTxt, entranceYearTxt, accomStateTxt, stayTimeTxt, maxDistanceTxt);
                        if (photoUrl!=null) {
                            uploadImage(photoUrl);
                        }
                        System.out.println(user.getPhotoUrl());
                        Toast.makeText(UserProfile.this, "KAYDEDİLDİ", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserProfile.this, "Veritabanı Hatası", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        editPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");
                launcher.launch(galleryIntent);
            }
        });
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK  && result.getData() != null) {
                    photoUrl = result.getData().getData();
                    getImageInImageView(photoUrl);
                    getContentResolver().takePersistableUriPermission(photoUrl, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
                }
            }
    );
    public void updateUser(String user_id, String email, String first_name, String last_name, String phone, String department, String entrance, String state, String duration, String distance) {
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
    private void getImageInImageView(Uri photoURL) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        uploadImg.setImageBitmap(bitmap);
    }
    private void readFromDB(String userid){
        databaseReference.child("users").child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot snapshot = task.getResult();
                        editTextEmail.setText(user.getEmail());
                        editTextEmail.setEnabled(false);
                        editTextName.setText(String.valueOf(snapshot.child("Name").getValue()));
                        editTextSurname.setText(String.valueOf(snapshot.child("Surname").getValue()));
                        editTextPhone.setText(String.valueOf(snapshot.child("Phone").getValue()));
                        editTextDepartment.setText(String.valueOf(snapshot.child("Department").getValue()));
                        String value = String.valueOf(snapshot.child("Entrance").getValue());
                        if(value.equals("")){
                            spinner1.setSelection(0);
                        }else{
                            spinner1.setSelection(getIndex(spinner1, value));
                        }

                        value = String.valueOf(snapshot.child("State").getValue());
                        if(value.equals("")){
                            spinner2.setSelection(0);
                        }else{
                            spinner2.setSelection(getIndex(spinner2, value));
                        }

                        value = String.valueOf(snapshot.child("Duration").getValue());
                        if(value.equals("")){
                            spinner3.setSelection(0);
                        }else{
                            spinner3.setSelection(getIndex(spinner3, value));
                        }
                        slider.setValue(Float.parseFloat(String.valueOf(snapshot.child("MaxDistance").getValue())));
                        String result = String.valueOf(snapshot.child("MaxDistance").getValue()) + " km";
                        sliderResult.setText(result);
                        if(snapshot.hasChild("PhotoURL")) {
                            String pictureURL = String.valueOf(snapshot.child("PhotoURL").getValue());
                            Picasso.get().load(pictureURL).into(uploadImg);
                        }
                    }
                }else{
                    Toast.makeText(UserProfile.this, "Failed Sadge", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
    private void uploadImage(Uri imageUri) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        StorageReference fileRef = storageRef.child("Profile Photos/" + userId + "/image.jpg");

        UploadTask uploadTask = fileRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                databaseReference.child("users").child(user.getUid()).child("PhotoURL").setValue(downloadUrl);
            }).addOnFailureListener(exception -> {
                Toast.makeText(this, "URL alınırken bir hata meydana geldi", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception -> {
            Toast.makeText(this, "Dosya kaydedilirken bir sorun oluştu", Toast.LENGTH_SHORT).show();
        });


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUrl)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(Task::isSuccessful);
    }
}
