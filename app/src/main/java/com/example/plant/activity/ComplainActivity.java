package com.example.plant.activity;

import static com.example.plant.R.layout.activity_complain;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ComplainActivity extends AppCompatActivity {

    AppCompatButton anonymousButton;
    AppCompatButton identityButton;
    AppCompatButton submitButton;

    LinearLayout identityLayout;
    LinearLayout submissionLayout;
    TextView identityName;
    TextView identityPhone;
    TextView identityEmail;
    TextView complainImageText;

    ImageView complainImage;
    ImageView backArrow;
    FrameLayout layoutComplainImage;

    EditText location;
    EditText complainDescription;

    ProgressBar progressBar;

    private String encodedImage;

    Boolean isClicked = false;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_complain);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        findSection();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        anonymousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                anonymousButton.setBackgroundResource(R.drawable.button_bg_2);
                anonymousButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                submissionLayout.setVisibility(View.VISIBLE);
                identityButton.setEnabled(false);
            }
        });

        identityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identityButton.setBackgroundResource(R.drawable.button_bg_2);
                identityButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                isClicked = true;
                identityLayout.setVisibility(View.VISIBLE);
                submissionLayout.setVisibility(View.VISIBLE);
                anonymousButton.setEnabled(false);

                String cUser = firebaseAuth.getCurrentUser().getUid();

                firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(cUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String userName = task.getResult().get("name").toString();
                            identityName.setText(userName);
                        }
                    }
                });

                firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(cUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String userPhone = task.getResult().get("phoneNumber").toString();
                            identityPhone.setText(userPhone);
                        }
                    }
                });

                firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(cUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String userEmail = task.getResult().get("email").toString();
                            identityEmail.setText(userEmail);
                        }
                    }
                });
            }
        });

        layoutComplainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = location.getText().toString();
                String description = complainDescription.getText().toString();
                String date = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date());

                if (isValidComplainDetails(address,description)){
                    if (isClicked == true){
                        complainWithIdentity(address,description,date);
                    } else {
                        complainAnonymously(address,description,date);
                    }
                }

            }
        });
    }

    private void complainAnonymously(String address, String description, String date) {

        FirebaseFirestore database= FirebaseFirestore.getInstance();
        HashMap<String, Object> complain= new HashMap<>();
        complain.put(Constants.KEY_DESCRIPTION, description);
        complain.put(Constants.KEY_DATE,date);
        complain.put(Constants.KEY_LOCATION,address);
        complain.put(Constants.KEY_BLOG_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_COMPLAIN).document().set(complain).
                addOnSuccessListener(documentReference -> {
                    showToast("Complain Submitted");
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                })
                .addOnFailureListener(exception -> {
                    showToast(exception.getMessage());

                });
    }

    private void complainWithIdentity(String address, String description, String date) {

        String currentUser = firebaseAuth.getCurrentUser().getUid();

        String name = identityName.getText().toString();
        String phone = identityPhone.getText().toString();
        String email = identityEmail.getText().toString();

        FirebaseFirestore database= FirebaseFirestore.getInstance();
        HashMap<String, Object> complain= new HashMap<>();
        complain.put(Constants.KEY_NAME, name);
        complain.put(Constants.KEY_PHONE_NUMBER, phone);
        complain.put(Constants.KEY_EMAIL, email);
        complain.put(Constants.KEY_DESCRIPTION, description);
        complain.put(Constants.KEY_DATE,date);
        complain.put(Constants.KEY_LOCATION,address);
        complain.put(Constants.KEY_BLOG_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_COMPLAIN_WITH_IDENTITY).document(currentUser).set(complain).
                addOnSuccessListener(documentReference -> {
                    showToast("Complain Submitted");
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                })
                .addOnFailureListener(exception -> {
                    showToast(exception.getMessage());

                });




    }

    private void findSection() {

        backArrow = findViewById(R.id.backArrow);
        anonymousButton = findViewById(R.id.anonymousButton);
        identityButton = findViewById(R.id.identityButton);
        submitButton = findViewById(R.id.submitpButton);
        identityLayout = findViewById(R.id.identityLayout);
        submissionLayout = findViewById(R.id.submissionLayout);
        layoutComplainImage = findViewById(R.id.layoutComplainImage);
        identityName = findViewById(R.id.identityNameText);
        identityPhone = findViewById(R.id.identityPhoneText);
        identityEmail = findViewById(R.id.identityEmailText);
        complainImage = findViewById(R.id.complainImage);
        location = findViewById(R.id.complainLocation);
        complainDescription = findViewById(R.id.complainDescription);
        complainImageText = findViewById(R.id.complainImageText);
    }

    private  String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap= Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes= byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri=result.getData().getData();
                        try{
                            InputStream inputStream=getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            complainImage.setImageBitmap(bitmap);
                            complainImageText.setVisibility(View.GONE);
                            encodedImage= encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    public Boolean isValidComplainDetails(String address, String description){
        if(encodedImage == null){
            showToast("Select Image");
            return false;
        }else if (address.trim().isEmpty()){
            showToast("Enter Location");
            return false;
        }else if (description.trim().isEmpty()) {
            showToast("Enter Description");
            return false;
        }else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            submitButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else{
            progressBar.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }
}