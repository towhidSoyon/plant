package com.example.plant.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

    FrameLayout layoutImage;
    EditText signupName;
    EditText signupEmail;
    EditText signupPhoneNumber;
    EditText signupLocation;
    EditText signupPassword;
    EditText signupConfirmPassword;

    CircleImageView imageProfile;
    TextView imageText;

    AppCompatButton signupButton;
    ProgressBar progressBar;
    TextView bottomText;

    private Uri uri = null;

    private static final int GALLERY_CODE = 2;

    /*private String encodedImage;*/

    FirebaseAuth mAuth;
    StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findSection();

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference();


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String phoneNumber = signupPhoneNumber.getText().toString();
                String location = signupLocation.getText().toString();
                String password = signupPassword.getText().toString();
                String confirmPassword = signupConfirmPassword.getText().toString();

                if (isValidSignUpDetails(userName, email, phoneNumber, location, password, confirmPassword)) {
                    createUser(userName, email, phoneNumber, location, password);
                }

            }
        });

        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageText.setVisibility(View.GONE);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void createUser(String userName, String email, String phoneNumber, String location, String password) {

        loading(true);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task1) {
                if (task1.isSuccessful()) {
                    if (uri != null) {
                        StorageReference filePath = mRef.child("User Images").child(uri.getLastPathSegment());

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
                                                    HashMap<String, Object> user = new HashMap<>();
                                                    user.put(Constants.KEY_NAME, userName);
                                                    user.put(Constants.KEY_EMAIL, email);
                                                    user.put(Constants.KEY_PASSWORD, password);
                                                    user.put(Constants.KEY_PHONE_NUMBER, phoneNumber);
                                                    user.put(Constants.KEY_LOCATION, location);
                                                    user.put(Constants.KEY_IMAGE, downloadUrl);
                                                    database.collection(Constants.KEY_COLLECTION_USERS).document(task1.getResult().getUser().getUid()).set(user).
                                                            addOnSuccessListener(documentReference -> {
                                                                loading(false);
                                                                showToast("Signup Successfully");
                                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
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


                } else {
                    loading(false);
                    showToast(task1.getException().getMessage());
                }
            }
        });
    }

    private void findSection() {
        layoutImage = findViewById(R.id.layoutBlogImage);
        signupName = findViewById(R.id.signupName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPhoneNumber = findViewById(R.id.signupPhoneNumber);
        signupLocation = findViewById(R.id.signupLocation);
        signupPassword = findViewById(R.id.signupPassword);
        signupConfirmPassword = findViewById(R.id.confirmPassword);

        signupButton = findViewById(R.id.signupButton);
        progressBar = findViewById(R.id.progressBar);
        bottomText = findViewById(R.id.bottomText);

        imageProfile = findViewById(R.id.blogImage);
        imageText = findViewById(R.id.textAddImage);
    }

    public void showToast(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

   /* private  String encodeImage(Bitmap bitmap){
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
                            imageProfile.setImageBitmap(bitmap);
                            imageText.setVisibility(View.GONE);
                            encodedImage= encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );*/

    public Boolean isValidSignUpDetails(String userName, String email, String phoneNumber, String location, String password, String confirmPassword) {
        /*if (encodedImage == null) {
            showToast("Select Profile Image");
            return false;
        } else*/ if (userName.trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (email.trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter Valid Email");
            return false;
        } else if (phoneNumber.length() > 11) {
            showToast("Enter valid Phone Number");
            return false;
        } else if (location.trim().isEmpty()) {
            showToast("Enter Location");
            return false;
        } else if (password.trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if (confirmPassword.trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!password.equals(confirmPassword)) {
            showToast("Password Must be same");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            signupButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            signupButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            uri = data.getData();

            imageProfile.setImageURI(uri);
        }
    }
}