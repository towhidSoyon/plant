package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plant.R;
import com.example.plant.activity.utilities.Constants;
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

        if (intentData.equals("Medicine Plant")){
            getMedicinePlantDetails();
        } else if (intentData.equals("Suitable Plants for AC Room")){
            getAcPlant();
        } else if (intentData.equals("Fruit Plant")){
            getFruitPlant();
        } else if (intentData.equals("Flower Plant")){
            getFlowerPlant();
        } else {

        }


    }

    private void getFruitPlant() {

        DatabaseReference getFruitPlantTitle= databaseReference.child("suggestedTrees").child("Fruit Plant").child("suggestedTreeTitle");

        getFruitPlantTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fruitTitle = snapshot.getValue(String.class);
                titleText.setText(fruitTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getFruitPlantImage = databaseReference.child("suggestedTrees").child("Fruit Plant").child("suggestedTreeImage");

        getFruitPlantImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fruitImageLink = snapshot.getValue(String.class);
                Picasso.get().load(fruitImageLink).into(suggestedTreeDetailsImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getFruitPlantDesc= databaseReference.child("suggestedTrees").child("Fruit Plant").child("suggestedTreeDescription");

        getFruitPlantDesc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fruitDescription = snapshot.getValue(String.class);
                descText.setText(fruitDescription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getFlowerPlant() {
    }

    private void getAcPlant() {

        DatabaseReference getACPlantTitle = databaseReference.child("suggestedTrees").child("AC Plant").child("suggestedTreeTitle");

        getACPlantTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String acDescription = snapshot.getValue(String.class);
                titleText.setText(acDescription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference getAcPlantImage = databaseReference.child("suggestedTrees").child("AC Plant").child("suggestedTreeImage");

        getAcPlantImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String acImageLink = snapshot.getValue(String.class);
                Picasso.get().load(acImageLink).into(suggestedTreeDetailsImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getAcPlantDesc= databaseReference.child("suggestedTrees").child("AC Plant").child("suggestedTreeDescription");

        getAcPlantDesc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String acDescription = snapshot.getValue(String.class);
                descText.setText(acDescription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMedicinePlantDetails() {

        DatabaseReference getMedicinePlantTitle= databaseReference.child("suggestedTrees").child("Medicine Plant").child("suggestedTreeTitle");

        getMedicinePlantTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String medicineTitle = snapshot.getValue(String.class);
                titleText.setText(medicineTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getMedicinePlantImage = databaseReference.child("suggestedTrees").child("Medicine Plant").child("suggestedTreeImage");

        getMedicinePlantImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String medicineImageLink = snapshot.getValue(String.class);
                Picasso.get().load(medicineImageLink).into(suggestedTreeDetailsImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference getMedicinePlantDesc= databaseReference.child("suggestedTrees").child("Medicine Plant").child("suggestedTreeDescription");

        getMedicinePlantDesc.addListenerForSingleValueEvent(new ValueEventListener() {
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

        titleText = findViewById(R.id.suggestedTreeDetailsTitle);
        descText = findViewById(R.id.suggestedTreeDetailsDesc);
    }
}