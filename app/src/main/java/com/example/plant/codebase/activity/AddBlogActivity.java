package com.example.plant.codebase.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.codebase.utilities.Constants;
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

public class AddBlogActivity extends AppCompatActivity {

    FrameLayout layoutBlogImage;

    ImageView blogImage;
    ImageView backArrow;
    TextView dateText;
    TextView textAdd;
    EditText blogTitle;
    EditText blogDescription;

    AppCompatButton postButton;

    //private String encodedImage;
    private Uri uri= null;
    private static final int GALLERY_CODE=2;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    StorageReference mRef;

    String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        findSection();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef= FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        String currentUser = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currentUserName = task.getResult().get("name").toString();
                }
            }
        });

        dateText.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutBlogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textAdd.setVisibility(View.GONE);
                Intent galleryIntent =new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = blogTitle.getText().toString();
                String date = dateText.getText().toString();
                String desc = blogDescription.getText().toString();
                if (isValidDetails(title,desc)){
                    addBlog(title,desc,date);
                }
            }
        });
    }

    private void addBlog(String title, String desc, String date) {

        if(uri != null) {
            StorageReference filePath= mRef.child("Blog Images").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        task.getResult().getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl= uri.toString();

                                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                                        HashMap<String, Object> blog = new HashMap<>();
                                        blog.put(Constants.KEY_NAME, currentUserName);
                                        blog.put(Constants.KEY_TITLE, title);
                                        blog.put(Constants.KEY_DESCRIPTION, desc);
                                        blog.put(Constants.KEY_DATE, date);
                                        blog.put(Constants.KEY_BLOG_IMAGE, downloadUrl);
                                        database.collection(Constants.KEY_COLLECTION_BLOGS).document().set(blog).
                                                addOnSuccessListener(documentReference -> {
                                                    showToast("Blog Posted");
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                })
                                                .addOnFailureListener(exception -> {
                                                    showToast(exception.getMessage());

                                                });
                                    }
                                });
                    }
                    else
                    {

                    }
                }

            });
        }


    }




    private void findSection() {

        layoutBlogImage = findViewById(R.id.layoutBlogImage);

        blogImage = findViewById(R.id.blogImage);
        backArrow = findViewById(R.id.backArrow);
        dateText = findViewById(R.id.dateText);
        textAdd = findViewById(R.id.textAddImage);
        blogTitle = findViewById(R.id.title);
        blogDescription = findViewById(R.id.description);

        postButton = findViewById(R.id.postButton);

    }

    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    public Boolean isValidDetails(String title, String desc){
        /*if(encodedImage == null){
            showToast("Select Profile Image");
            return false;
        }else*/ if (title.trim().isEmpty()){
            showToast("Enter Title");
            return false;
        }else if (desc.trim().isEmpty()){
            showToast("Enter Description");
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_CODE && resultCode == RESULT_OK){
            uri =data.getData();

            blogImage.setImageURI(uri);
        }
    }
}