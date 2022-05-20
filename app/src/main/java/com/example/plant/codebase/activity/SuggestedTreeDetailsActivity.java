package com.example.plant.codebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.plant.R;
import com.example.plant.codebase.utilities.Constants;
import com.example.plant.codebase.utilities.CustomProgressDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SuggestedTreeDetailsActivity extends AppCompatActivity {

    ImageView backArrow;
    ImageView suggestedTreeDetailsImage;

    TextView titleText;
    TextView descText;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_tree_details);

        findSection();


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        String intentData = getIntent().getStringExtra(Constants.KEY_SUGGESTED_TREE_RESULT);

        String plantText;

        if (intentData.equals("Medicine Plant")){
            plantText = "Medicine Plant";
            getPlant(plantText);
            progressBar.setVisibility(View.GONE);
        } else if (intentData.equals("Suitable Plants for AC Room")){
            plantText = "AC Plant";
            getPlant(plantText);
            progressBar.setVisibility(View.GONE);
        } else if (intentData.equals("Fruit Plant")){
            plantText = "Fruit Plant";
            getPlant(plantText);
            progressBar.setVisibility(View.GONE);
        } else if (intentData.equals("Flower Plant")){
            plantText = "Flower Plant";
            getPlant(plantText);
            progressBar.setVisibility(View.GONE);
        } else if(intentData.equals("Benefits of Brinjal Plant")) {
            plantText = "Brinjal Plant";
            getPlant(plantText);
            progressBar.setVisibility(View.GONE);
        } else {

        }


    }

    private void getPlant(String plant){

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference getTitle= databaseReference.child("suggestedTrees").child(plant).child("suggestedTreeTitle");

        getTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Title = snapshot.getValue(String.class);
                titleText.setText(Title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getImage = databaseReference.child("suggestedTrees").child(plant).child("suggestedTreeImage");

        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ImageLink = snapshot.getValue(String.class);
                Picasso.get().load(ImageLink).into(suggestedTreeDetailsImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getDesc= databaseReference.child("suggestedTrees").child(plant).child("suggestedTreeDescription");

        getDesc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String description = snapshot.getValue(String.class);
                descText.setText(description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findSection() {
        backArrow = findViewById(R.id.backArrow);
        suggestedTreeDetailsImage = findViewById(R.id.suggestedTreeDetailsImage);
        progressBar = findViewById(R.id.progressBarOfLayout);
        titleText = findViewById(R.id.suggestedTreeDetailsTitle);
        descText = findViewById(R.id.suggestedTreeDetailsDesc);
    }
}