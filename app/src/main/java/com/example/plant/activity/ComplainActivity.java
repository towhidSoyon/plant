package com.example.plant.activity;

import static com.example.plant.R.layout.activity_complain;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.plant.R;
import com.example.plant.activity.adapter.SliderAdapter;
import com.example.plant.activity.model.SlideInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class ComplainActivity extends AppCompatActivity {

    AppCompatButton anonymousButton;
    AppCompatButton identityButton;

    ImageView backArrow;

    SliderView sliderView;
    SliderAdapter adapter;
    private DatabaseReference SlideRef;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_complain);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        findSection();

        backArrow.setOnClickListener(view -> onBackPressed());

        anonymousButton.setOnClickListener(view -> {
            startActivity(new Intent(ComplainActivity.this, ComplainAnonymouslyActivity.class));
            finish();
        });

        identityButton.setOnClickListener(view -> {
            startActivity(new Intent(ComplainActivity.this, ComplainWithIdentityActivity.class));
            finish();
        });

       /* layoutComplainImage.setOnClickListener(new View.OnClickListener() {
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
        });*/

        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(R.color.colorPrimary);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(6); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        final List<SlideInfo> sliderItemList = new ArrayList<>();

        SlideRef = FirebaseDatabase.getInstance().getReference().child("DeforestationSlideShow");

        SlideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        SlideInfo posts = postSnapshot.getValue(SlideInfo.class);

                        String key = postSnapshot.getKey();
                        String description = snapshot.child(key).child("description").getValue().toString();
                        String url = snapshot.child(key).child("imageUrl").getValue().toString();

                        posts.setImageKey(key);
                        posts.setImageUrl(url);
                        posts.setDescription(description);

                        sliderItemList.add(posts);

                    }
                    adapter.renewItems((ArrayList<SlideInfo>) sliderItemList);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

   /* private void complainAnonymously(String address, String description, String date) {

        FirebaseFirestore database= FirebaseFirestore.getInstance();
        HashMap<String, Object> complain= new HashMap<>();
        complain.put(Constants.KEY_DESCRIPTION, description);
        complain.put(Constants.KEY_DATE,date);
        complain.put(Constants.KEY_LOCATION,address);
        complain.put(Constants.KEY_BLOG_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_COMPLAIN).document().set(complain).
                addOnSuccessListener(documentReference -> {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                })
                .addOnFailureListener(exception -> {
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
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                })
                .addOnFailureListener(exception -> {

                });




    }*/

    private void findSection() {

        backArrow = findViewById(R.id.backArrow);
        anonymousButton = findViewById(R.id.anonymousButton);
        identityButton = findViewById(R.id.identityButton);

    }




}