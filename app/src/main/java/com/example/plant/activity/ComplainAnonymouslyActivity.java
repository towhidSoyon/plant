package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ComplainAnonymouslyActivity extends AppCompatActivity {

    ImageView backArrow;
    LinearLayout submissionAnonymousLayout;

    AppCompatButton submitAnonymousButton;

    TextView complainAnonymousImageText;

    ImageView complainAnonymousImage;
    FrameLayout layoutComplainAnonymousImage;

    EditText complainAnonymousLocation;
    EditText complainAnonymousDescription;

    private static final int GALLERY_CODE = 2;

    StorageReference mRef;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Uri uri = null;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_anonymously);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference();

        findSection();

        backArrow.setOnClickListener(view -> onBackPressed());

        layoutComplainAnonymousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complainAnonymousImageText.setVisibility(View.GONE);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        submitAnonymousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = complainAnonymousLocation.getText().toString();
                String description = complainAnonymousDescription.getText().toString();
                String date = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date());

                if (isValidComplainDetails(address, description)) {
                    complainAnonymous(address, description, date);
                }
            }
        });
    }

    private void complainAnonymous(String address, String description, String date) {
        loading(true);

        if (uri != null) {
            StorageReference filePath = mRef.child("ComplainAnonymousImage").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();

                                        FirebaseFirestore database= FirebaseFirestore.getInstance();
                                        HashMap<String, Object> complainAnonymous= new HashMap<>();
                                        complainAnonymous.put(Constants.KEY_DESCRIPTION, description);
                                        complainAnonymous.put(Constants.KEY_DATE,date);
                                        complainAnonymous.put(Constants.KEY_LOCATION,address);
                                        complainAnonymous.put(Constants.KEY_COMPLAIN_ANONYMOUS_IMAGE, downloadUrl);
                                        database.collection(Constants.KEY_COLLECTION_COMPLAIN_ANONYMOUS).document().set(complainAnonymous).
                                                addOnSuccessListener(documentReference -> {
                                                    loading(false);
                                                    showToast("Complain Submitted");
                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
        submissionAnonymousLayout = findViewById(R.id.submissionAnonymousLayout);
        submitAnonymousButton = findViewById(R.id.submitAnonymousButton);
        complainAnonymousImageText = findViewById(R.id.complainAnonymousImageText);
        complainAnonymousImage = findViewById(R.id.complainAnonymousImage);
        layoutComplainAnonymousImage = findViewById(R.id.layoutComplainAnonymousImage);
        complainAnonymousLocation = findViewById(R.id.complainAnonymousLocation);
        complainAnonymousDescription = findViewById(R.id.complainAnonymousDescription);
        progressBar = findViewById(R.id.progressBarAnonymous);

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
            submitAnonymousButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            submitAnonymousButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            uri = data.getData();

            complainAnonymousImage.setImageURI(uri);
        }
    }
}