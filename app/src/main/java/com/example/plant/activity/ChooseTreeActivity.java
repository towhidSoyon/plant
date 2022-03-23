package com.example.plant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.utilities.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ChooseTreeActivity extends AppCompatActivity {

    ImageView backArrow;
    LinearLayout chooseSoilLayout;
    LinearLayout chooseWaterSupplyLayout;
    LinearLayout chooseSunlightLayout;
    LinearLayout choosePlantLayout;

    EditText soilText;
    EditText temperatureText;
    EditText placeText;
    EditText waterText;
    EditText sunlightText;
    EditText plantText;
    
    AppCompatButton submitButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tree);

        findSection();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
        chooseSoilLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoilDialog();
            }
        });

        chooseWaterSupplyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWaterSupplyDialog();
            }
        });

        chooseSunlightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSunlightDialog();
            }
        });

        choosePlantLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlantDialog();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String soil = soilText.getText().toString();
                String temperature = temperatureText.getText().toString();
                String place = placeText.getText().toString();
                String waterSupply = waterText.getText().toString();
                String sunlight = sunlightText.getText().toString();
                String plants = plantText.getText().toString();

                if (isValidInformation(soil,temperature,place,waterSupply,sunlight,plants)){
                    Intent intent=new Intent(getApplicationContext(), ChooseTreeResultActivity.class);
                    intent.putExtra(Constants.KEY_SOIL, soil);
                    intent.putExtra(Constants.KEY_TEMPERATURE, temperature);
                    intent.putExtra(Constants.KEY_PLACE, place);
                    intent.putExtra(Constants.KEY_WATER_SUPPLY, waterSupply);
                    intent.putExtra(Constants.KEY_SUNLIGHT, sunlight);
                    intent.putExtra(Constants.KEY_PLANTS, plants);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private Boolean isValidInformation(String soil, String temperature, String place, String waterSupply, String sunlight, String plants) {

        if (soil.trim().isEmpty()){
            showToast("Enter Soil Type");
            return false;
        }else if (temperature.trim().isEmpty()){
            showToast("Enter Temperature");
            return false;
        }else if (place.trim().isEmpty()){
            showToast("Enter Location");
            return false;
        }else if (waterSupply.trim().isEmpty()){
            showToast("Enter the Amount of Water Supply");
            return false;
        }else if (sunlight.trim().isEmpty()){
            showToast("Enter the Amount of Water Supply");
            return false;
        }
        else {
            return true;
        }
    }

    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    private void showPlantDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this,R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_plants, findViewById(R.id.plantDialogContainer));

        sheetView.findViewById(R.id.fruitLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                plantText.setText("Fruit");
            }
        });

        sheetView.findViewById(R.id.flowerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                plantText.setText("Flowers");
            }
        });

        sheetView.findViewById(R.id.medicineLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                plantText.setText("Medicine");
            }
        });

        sheetView.findViewById(R.id.economicalLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                plantText.setText("Economical");
            }
        });

        sheetView.findViewById(R.id.allLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                plantText.setText("All");
            }
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();

    }

    private void showSunlightDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this,R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_sunlight, findViewById(R.id.sunlightDialogContainer));

        sheetView.findViewById(R.id.lowSunlightLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                sunlightText.setText("Low");
            }
        });

        sheetView.findViewById(R.id.averageSunlightLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                sunlightText.setText("Average");
            }
        });

        sheetView.findViewById(R.id.goodSunlightLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                sunlightText.setText("Good");
            }
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();
    }

    private void showWaterSupplyDialog() {

        BottomSheetDialog sheetDialog = new BottomSheetDialog(this,R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_water_supply, findViewById(R.id.waterDialogContainer));

        sheetView.findViewById(R.id.lowLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                waterText.setText("Low");
            }
        });

        sheetView.findViewById(R.id.averageLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                waterText.setText("Average");
            }
        });

        sheetView.findViewById(R.id.goodLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                waterText.setText("Good");
            }
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();
    }

    private void showSoilDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this,R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_soil, findViewById(R.id.soilDialogContainer));

        sheetView.findViewById(R.id.beleSoilLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                soilText.setText("BELE SOIL");
            }
        });

        sheetView.findViewById(R.id.doashSoilLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                soilText.setText("DO-ASH SOIL");
            }
        });

        sheetView.findViewById(R.id.etelSoilLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                soilText.setText("ETEL SOIL");
            }
        });

        sheetView.findViewById(R.id.beleDoashSoilLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                soilText.setText("BELE-DO-ASH SOIL");
            }
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();
    }

    private void findSection() {

        backArrow = findViewById(R.id.backArrow);
        
        chooseSoilLayout = findViewById(R.id.inputSoilLayout);
        chooseWaterSupplyLayout = findViewById(R.id.inputWaterLayout);
        chooseSunlightLayout = findViewById(R.id.inputSunlightLayout);
        choosePlantLayout = findViewById(R.id.inputPlantLayout);

        soilText = findViewById(R.id.soilEditText);
        temperatureText = findViewById(R.id.temperatureEditText);
        placeText = findViewById(R.id.placeEditText);
        waterText = findViewById(R.id.waterEditText);
        sunlightText = findViewById(R.id.sunlightEditText);
        plantText = findViewById(R.id.plantEditText);

        submitButton = findViewById(R.id.chooseTreeSubmitButton);

        progressBar = findViewById(R.id.progressBar);
    }


}
