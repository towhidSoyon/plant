package com.example.plant.codebase.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.codebase.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView editProfileImage;
    ImageView backArrow;
    ImageView editButton;
    TextInputLayout editFullName;
    TextInputLayout editEmail;
    TextInputLayout editPhoneNumber;
    TextInputLayout editAddress;
    AppCompatButton doneButton;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference mRef;

    private Uri uri = null;

    private static final int GALLERY_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findSection();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference();
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
                    editProfileImage.setImageBitmap(bitmap);*/
                    Picasso.get().load(img).into(editProfileImage);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().get("name").toString();
                    editFullName.getEditText().setText(userName);
                }
            }
        });


        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userEmail = task.getResult().get("email").toString();
                    editEmail.getEditText().setText(userEmail);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userPhoneNumber = task.getResult().get("phoneNumber").toString();
                    editPhoneNumber.getEditText().setText(userPhoneNumber);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userAddress = task.getResult().get("location").toString();
                    editAddress.getEditText().setText(userAddress);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editFullName.getEditText().getText().toString();
                String email = editEmail.getEditText().getText().toString();
                String phoneNumber = editPhoneNumber.getEditText().getText().toString();
                String location = editAddress.getEditText().getText().toString();

                if (isValidSignUpDetails(userName,email,phoneNumber,location)){
                    updateUser(userName, email, phoneNumber, location);
                }
            }
        });


    }

    private void updateUser(String userName, String email, String phoneNumber, String location) {

        String currentUser = mAuth.getCurrentUser().getUid();

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

                                        FirebaseFirestore database= FirebaseFirestore.getInstance();
                                        HashMap<String, Object> user= new HashMap<>();
                                        user.put(Constants.KEY_NAME, userName);
                                        user.put(Constants.KEY_EMAIL, email);
                                        user.put(Constants.KEY_PHONE_NUMBER, phoneNumber);
                                        user.put(Constants.KEY_LOCATION, location);
                                        user.put(Constants.KEY_IMAGE, downloadUrl);
                                        database.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).set(user).
                                                addOnSuccessListener(documentReference -> {
                                                    showToast("Updated Successfully");
                                                    startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                                                })
                                                .addOnFailureListener(exception -> {
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

        editProfileImage = findViewById(R.id.editProfileImage);
        backArrow = findViewById(R.id.backArrow);
        editButton = findViewById(R.id.editButton);
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editAddress = findViewById(R.id.editAddress);
        doneButton = findViewById(R.id.updateButton);
    }

    /*private  String encodeImage(Bitmap bitmap){
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
                            editProfileImage.setImageBitmap(bitmap);
                            encodedImage= encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );*/

    public Boolean isValidSignUpDetails(String userName, String email, String phoneNumber, String location){
        /*if(encodedImage == null){
            showToast("Select Profile Image");
            return false;
        }else*/ if (userName.trim().isEmpty()){
            showToast("Enter name");
            return false;
        }else if (email.trim().isEmpty()){
            showToast("Enter email");
            return false;
        }else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast("Enter Valid Email");
            return false;
        }else if ( phoneNumber.length()>11 ){
            showToast("Enter valid Phone Number");
            return false;
        }else if (location.trim().isEmpty()){
            showToast("Enter Location");
            return false;
        }else {
            return true;
        }
    }

    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            uri = data.getData();

            editProfileImage.setImageURI(uri);
        }
    }
}