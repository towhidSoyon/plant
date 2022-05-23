package com.example.plant.codebase.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.codebase.utilities.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SuggestedTreeActivity extends AppCompatActivity  {

    /*private RecyclerView suggestedTreeRecyclerView;*/

    ImageView backArrow;

    AppCompatButton medicinePlantButton;
    AppCompatButton acPlantButton;
    AppCompatButton fruitPlantButton;
    AppCompatButton flowerPlantButton;
    AppCompatButton brinjalPlantButton;
   /* AppCompatButton b3PlantButton;
    AppCompatButton b4PlantButton;
    AppCompatButton b5PlantButton;*/

    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_tree);

        firebaseFirestore = FirebaseFirestore.getInstance();

        findSection();

        backArrow.setOnClickListener(view -> onBackPressed());

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        /*getSuggestedTree();*/

        medicinePlantButton.setOnClickListener(view -> {

            Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
            intent.putExtra(Constants.KEY_SUGGESTED_TREE_RESULT, medicinePlantButton.getText().toString() );
            startActivity(intent);

        });

        acPlantButton.setOnClickListener(view -> {

            Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
            intent.putExtra(Constants.KEY_SUGGESTED_TREE_RESULT, acPlantButton.getText().toString() );
            startActivity(intent);

        });

        fruitPlantButton.setOnClickListener(view -> {
            Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
            intent.putExtra(Constants.KEY_SUGGESTED_TREE_RESULT, fruitPlantButton.getText().toString() );
            startActivity(intent);
        });

        flowerPlantButton.setOnClickListener(view -> {
            Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
            intent.putExtra(Constants.KEY_SUGGESTED_TREE_RESULT, flowerPlantButton.getText().toString() );
            startActivity(intent);
        });

        brinjalPlantButton.setOnClickListener(view -> {
            Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
            intent.putExtra(Constants.KEY_SUGGESTED_TREE_RESULT, brinjalPlantButton.getText().toString() );
            startActivity(intent);
        });

        /*b3PlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Will be added soon");
            }
        });

        b4PlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Will be added soon");
            }
        });

        b5PlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Will be added soon");
            }
        });*/


    }

  /*  private void getSuggestedTree() {

        firebaseFirestore.collection(Constants.KEY_COLLECTION_SUGGESTED_TREE)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null){
                        List<SuggestedTree> suggestedTreeList=new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            SuggestedTree tree=new SuggestedTree();
                            tree.suggestedTreeImage = queryDocumentSnapshot.getString(Constants.KEY_SUGGESTED_IMAGE);
                            tree.suggestedTreeTitle = queryDocumentSnapshot.getString(Constants.KEY_TITLE);
                            tree.suggestedTreeDesc = queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION);
                            suggestedTreeList.add(tree);
                        }
                        if (suggestedTreeList.size()>0){
                            SuggestedTreeAdapter adapter=new SuggestedTreeAdapter(suggestedTreeList,this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            suggestedTreeRecyclerView.setAdapter(adapter);
                            suggestedTreeRecyclerView.setLayoutManager(linearLayoutManager);
                        }
                        else {
                            showToast("Empty Suggestion");
                        }
                    }
                    else {
                        showToast(task.getException().getMessage().toString());
                    }
                });
    }*/

    private void findSection() {

        /*suggestedTreeRecyclerView = findViewById(R.id.suggestedTreeRecyclerView);*/
        backArrow = findViewById(R.id.backArrow);
        medicinePlantButton = findViewById(R.id.buttonMedicinePlant);
        acPlantButton = findViewById(R.id.buttonACPlant);
        fruitPlantButton = findViewById(R.id.buttonFruitPlant);
        flowerPlantButton = findViewById(R.id.buttonFlowerPlant);
        brinjalPlantButton = findViewById(R.id.brinjalPlant);
        /*b3PlantButton = findViewById(R.id.button3Plant);
        b4PlantButton = findViewById(R.id.button4Plant);
        b5PlantButton = findViewById(R.id.button5Plant);*/
    }


    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onSuggestedTreeClicked(SuggestedTree suggestedTreeList) {
        Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
        intent.putExtra(Constants.KEY_COLLECTION_SUGGESTED_TREE, suggestedTreeList);
        startActivity(intent);
    }*/
}