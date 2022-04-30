package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.plant.R;
import com.example.plant.activity.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profileImage;
    ImageView backArrow;
    TextInputLayout fullName;
    TextInputLayout email;
    TextInputLayout phoneNumber;
    TextInputLayout address;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findSection();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String currentUser = mAuth.getCurrentUser().getUid();

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String img = task.getResult().get("image").toString();
                    /*byte[] bytes = Base64.decode(img, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profileImage.setImageBitmap(bitmap);*/
                    Picasso.get().load(img).into(profileImage);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().get("name").toString();
                    fullName.getEditText().setText(userName);
                }
            }
        });


        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userEmail = task.getResult().get("email").toString();
                    email.getEditText().setText(userEmail);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userPhoneNumber = task.getResult().get("phoneNumber").toString();
                    phoneNumber.getEditText().setText(userPhoneNumber);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userAddress = task.getResult().get("location").toString();
                    address.getEditText().setText(userAddress);
                }
            }
        });

    }

    private void findSection() {

        profileImage = findViewById(R.id.profileImage);
        backArrow = findViewById(R.id.backArrow);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
    }
}