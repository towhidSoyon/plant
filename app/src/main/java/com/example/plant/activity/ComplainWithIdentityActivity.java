package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ComplainWithIdentityActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 2;
    ProgressBar progressBar;
    AppCompatButton submitIdentityButton;

    LinearLayout identityLayout;
    LinearLayout submissionIdentityLayout;
    TextView identityName;
    TextView identityPhone;
    TextView identityEmail;
    TextView complainIdentityImageText;

    ImageView complainIdentityImage;
    ImageView backArrow;
    FrameLayout layoutComplainIdentityImage;

    EditText complainIdentityLocation;
    EditText complainIdentityDescription;
    StorageReference mRef;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Uri uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_with_identity);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference();

        findSection();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

        layoutComplainIdentityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complainIdentityImageText.setVisibility(View.GONE);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        submitIdentityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = complainIdentityLocation.getText().toString();
                String description = complainIdentityDescription.getText().toString();
                String date = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date());

                if (isValidComplainDetails(address, description)) {
                    complainWithIdentity(address, description, date);
                }
            }
        });
    }

    private void complainWithIdentity(String address, String description, String date) {
        loading(true);

        String currentUser = firebaseAuth.getCurrentUser().getUid();

        String name = identityName.getText().toString();
        String phone = identityPhone.getText().toString();
        String email = identityEmail.getText().toString();

        if (uri != null) {
            StorageReference filePath = mRef.child("ComplainIdentityImage").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();

                                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                                        HashMap<String, Object> complain = new HashMap<>();
                                        complain.put(Constants.KEY_NAME, name);
                                        complain.put(Constants.KEY_PHONE_NUMBER, phone);
                                        complain.put(Constants.KEY_EMAIL, email);
                                        complain.put(Constants.KEY_DESCRIPTION, description);
                                        complain.put(Constants.KEY_DATE, date);
                                        complain.put(Constants.KEY_LOCATION, address);
                                        complain.put(Constants.KEY_BLOG_IMAGE, downloadUrl);
                                        database.collection(Constants.KEY_COLLECTION_COMPLAIN_WITH_IDENTITY).document(currentUser).set(complain).
                                                addOnSuccessListener(documentReference -> {
                                                    loading(false);
                                                    showToast("Complain Submitted");
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    finish();
                                                })
                                                .addOnFailureListener(exception -> {
                                                    loading(false);
                                                    showToast(exception.getMessage());

                                                });
                                    }
                                });
                    } else {

                    }
                }

            });

        }
    }

    private void findSection() {

        backArrow = findViewById(R.id.backArrow);
        identityLayout = findViewById(R.id.identityLayout);
        submissionIdentityLayout = findViewById(R.id.submissionIdentityLayout);
        layoutComplainIdentityImage = findViewById(R.id.layoutComplainIdentityImage);
        identityName = findViewById(R.id.identityNameText);
        identityPhone = findViewById(R.id.identityPhoneText);
        identityEmail = findViewById(R.id.identityEmailText);
        complainIdentityImage = findViewById(R.id.complainIdentityImage);
        complainIdentityLocation = findViewById(R.id.complainIdentityLocation);
        complainIdentityDescription = findViewById(R.id.complainIdentityDescription);
        complainIdentityImageText = findViewById(R.id.complainImageText);
        progressBar = findViewById(R.id.progressBar);
        submitIdentityButton = findViewById(R.id.submitIdentityButton);

    }

    public void showToast(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    public Boolean isValidComplainDetails(String address, String description) {
        if (address.trim().isEmpty()) {
            showToast("Enter Location");
            return false;
        } else if (description.trim().isEmpty()) {
            showToast("Enter Description");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            submitIdentityButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            submitIdentityButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            uri = data.getData();

            complainIdentityImage.setImageURI(uri);
        }
    }
}