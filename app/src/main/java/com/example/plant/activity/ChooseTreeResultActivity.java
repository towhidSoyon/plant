package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChooseTreeResultActivity extends AppCompatActivity {

    ImageView backArrow;
    ImageView fruitTreeImage;
    ImageView flowerTreeImage;
    ImageView medicineTreeImage;
    ImageView economicTreeImage;

    TextView fruitTree;
    TextView flowerTree;
    TextView medicineTree;
    TextView economicalTree;

    TextView fruitTreeResult;
    TextView flowerTreeResult;
    TextView medicineTreeResult;
    TextView economicalTreeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tree_result);

        findSection();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference getFruitImage = databaseReference.child("chooseTreeResultImage").child("Fruit Plant");

        getFruitImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(fruitTreeImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error Loading Image");
            }
        });

        DatabaseReference getFlowerImage = databaseReference.child("chooseTreeResultImage").child("Flower Plant");

        getFlowerImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String flowerLink = snapshot.getValue(String.class);
                Picasso.get().load(flowerLink).into(flowerTreeImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error Loading Image");
            }
        });

        DatabaseReference getMedicineImage = databaseReference.child("chooseTreeResultImage").child("Medicine Plant");

        getMedicineImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String medicineLink = snapshot.getValue(String.class);
                Picasso.get().load(medicineLink).into(medicineTreeImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error Loading Image");
            }
        });

        DatabaseReference getEconomicImage = databaseReference.child("chooseTreeResultImage").child("Economical Plant");

        getEconomicImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String economicLink = snapshot.getValue(String.class);
                Picasso.get().load(economicLink).into(economicTreeImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error Loading Image");
            }
        });

        String soil = getIntent().getStringExtra(Constants.KEY_SOIL);
        String temperature = getIntent().getStringExtra(Constants.KEY_TEMPERATURE);
        String place = getIntent().getStringExtra(Constants.KEY_PLACE);
        String water = getIntent().getStringExtra(Constants.KEY_WATER_SUPPLY);

        String sunlight = getIntent().getStringExtra(Constants.KEY_SUNLIGHT);
        String plant = getIntent().getStringExtra(Constants.KEY_PLANTS);

        if (plant.equals("Fruit")) {

            flowerTree.setVisibility(View.GONE);
            flowerTreeImage.setVisibility(View.GONE);
            flowerTreeResult.setVisibility(View.GONE);
            medicineTree.setVisibility(View.GONE);
            medicineTreeImage.setVisibility(View.GONE);
            medicineTreeResult.setVisibility(View.GONE);
            economicalTree.setVisibility(View.GONE);
            economicTreeImage.setVisibility(View.GONE);
            economicalTreeResult.setVisibility(View.GONE);
            DatabaseReference getFruitTree = databaseReference.child("chooseTreeResult").child("Set_One").child("Fruit Plant");
            getFruitTree.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String fruits = snapshot.getValue(String.class);
                    fruitTreeResult.setText(fruits);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (plant.equals("Flowers")) {

            fruitTree.setVisibility(View.GONE);
            fruitTreeImage.setVisibility(View.GONE);
            fruitTreeResult.setVisibility(View.GONE);
            medicineTree.setVisibility(View.GONE);
            medicineTreeImage.setVisibility(View.GONE);
            medicineTreeResult.setVisibility(View.GONE);
            economicalTree.setVisibility(View.GONE);
            economicTreeImage.setVisibility(View.GONE);
            economicalTreeResult.setVisibility(View.GONE);

            DatabaseReference getFlowerTree = databaseReference.child("chooseTreeResult").child("Set_Two").child("Flower Plant");
            getFlowerTree.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String flower = snapshot.getValue(String.class);
                    flowerTreeResult.setText(flower);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (plant.equals("Medicine")) {

            fruitTree.setVisibility(View.GONE);
            fruitTreeImage.setVisibility(View.GONE);
            fruitTreeResult.setVisibility(View.GONE);
            flowerTree.setVisibility(View.GONE);
            flowerTreeImage.setVisibility(View.GONE);
            flowerTreeResult.setVisibility(View.GONE);
            economicalTree.setVisibility(View.GONE);
            economicTreeImage.setVisibility(View.GONE);
            economicalTreeResult.setVisibility(View.GONE);

            DatabaseReference getMedicineTree = databaseReference.child("chooseTreeResult").child("Set_Three").child("Medicine Plant");
            getMedicineTree.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String medicineTree = snapshot.getValue(String.class);
                    medicineTreeResult.setText(medicineTree);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (plant.equals("Economical")) {

            fruitTree.setVisibility(View.GONE);
            fruitTreeImage.setVisibility(View.GONE);
            fruitTreeResult.setVisibility(View.GONE);
            flowerTree.setVisibility(View.GONE);
            flowerTreeImage.setVisibility(View.GONE);
            flowerTreeResult.setVisibility(View.GONE);
            medicineTree.setVisibility(View.GONE);
            medicineTreeImage.setVisibility(View.GONE);
            medicineTreeResult.setVisibility(View.GONE);

            DatabaseReference getMedicineTree = databaseReference.child("chooseTreeResult").child("Set_Three").child("Economical Plant");
            getMedicineTree.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String medicineTree = snapshot.getValue(String.class);
                    medicineTreeResult.setText(medicineTree);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            if (soil.equals("BELE SOIL")) {

                DatabaseReference getFruitTree = databaseReference.child("chooseTreeResult").child("Set_One").child("Fruit Plant");
                getFruitTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fruits = snapshot.getValue(String.class);
                        fruitTreeResult.setText(fruits);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getFlowerTree = databaseReference.child("chooseTreeResult").child("Set_One").child("Flower Plant");
                getFlowerTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String flower = snapshot.getValue(String.class);
                        flowerTreeResult.setText(flower);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getMedicineTree = databaseReference.child("chooseTreeResult").child("Set_One").child("Medicine Plant");
                getMedicineTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String medicineTree = snapshot.getValue(String.class);
                        medicineTreeResult.setText(medicineTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getEconomicalTree = databaseReference.child("chooseTreeResult").child("Set_One").child("Economical Plant");
                getEconomicalTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String economicalTree = snapshot.getValue(String.class);
                        economicalTreeResult.setText(economicalTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else if (soil.equals("DO-ASH SOIL")) {

                DatabaseReference getFruitTree = databaseReference.child("chooseTreeResult").child("Set_Two").child("Fruit Plant");
                getFruitTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fruits = snapshot.getValue(String.class);
                        fruitTreeResult.setText(fruits);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getFlowerTree = databaseReference.child("chooseTreeResult").child("Set_Two").child("Flower Plant");
                getFlowerTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String flower = snapshot.getValue(String.class);
                        flowerTreeResult.setText(flower);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getMedicineTree = databaseReference.child("chooseTreeResult").child("Set_Two").child("Medicine Plant");
                getMedicineTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String medicineTree = snapshot.getValue(String.class);
                        medicineTreeResult.setText(medicineTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getEconomicalTree = databaseReference.child("chooseTreeResult").child("Set_Two").child("Economical Plant");
                getEconomicalTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String economicalTree = snapshot.getValue(String.class);
                        economicalTreeResult.setText(economicalTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            } else if (soil.equals("ETEL SOIL")) {

                DatabaseReference getFruitTree = databaseReference.child("chooseTreeResult").child("Set_Three").child("Fruit Plant");
                getFruitTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fruits = snapshot.getValue(String.class);
                        fruitTreeResult.setText(fruits);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getFlowerTree = databaseReference.child("chooseTreeResult").child("Set_Three").child("Flower Plant");
                getFlowerTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String flower = snapshot.getValue(String.class);
                        flowerTreeResult.setText(flower);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getMedicineTree = databaseReference.child("chooseTreeResult").child("Set_Three").child("Medicine Plant");
                getMedicineTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String medicineTree = snapshot.getValue(String.class);
                        medicineTreeResult.setText(medicineTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getEconomicalTree = databaseReference.child("chooseTreeResult").child("Set_Three").child("Economical Plant");
                getEconomicalTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String economicalTree = snapshot.getValue(String.class);
                        economicalTreeResult.setText(economicalTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            } else {

                DatabaseReference getFruitTree = databaseReference.child("chooseTreeResult").child("Set_Four").child("Fruit Plant");
                getFruitTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fruits = snapshot.getValue(String.class);
                        fruitTreeResult.setText(fruits);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getFlowerTree = databaseReference.child("chooseTreeResult").child("Set_Four").child("Flower Plant");
                getFlowerTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String flower = snapshot.getValue(String.class);
                        flowerTreeResult.setText(flower);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getMedicineTree = databaseReference.child("chooseTreeResult").child("Set_Four").child("Medicine Plant");
                getMedicineTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String medicineTree = snapshot.getValue(String.class);
                        medicineTreeResult.setText(medicineTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference getEconomicalTree = databaseReference.child("chooseTreeResult").child("Set_Four").child("Economical Plant");
                getEconomicalTree.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String economicalTree = snapshot.getValue(String.class);
                        economicalTreeResult.setText(economicalTree);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        }


    }

    private void findSection() {

        backArrow = findViewById(R.id.backArrow);
        fruitTreeImage = findViewById(R.id.fruitTreeImage);
        flowerTreeImage = findViewById(R.id.flowerTreeImage);
        medicineTreeImage = findViewById(R.id.medicineTreeImage);
        economicTreeImage = findViewById(R.id.economicalTreeImage);
        fruitTreeResult = findViewById(R.id.fruitTreeResult);
        flowerTreeResult = findViewById(R.id.flowerTreeResult);
        medicineTreeResult = findViewById(R.id.medicineTreeResult);
        economicalTreeResult = findViewById(R.id.economicalTreeResult);

        fruitTree = findViewById(R.id.fruitTreeText);
        flowerTree = findViewById(R.id.flowerTreeText);
        medicineTree = findViewById(R.id.medicineTreeText);
        economicalTree = findViewById(R.id.economicalTreeText);

    }

    public void showToast(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }
}