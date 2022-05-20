package com.example.plant.codebase.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.plant.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ReminderActivity extends AppCompatActivity {

    ImageView backArrow;

    LinearLayout plantType;

    EditText reminderPlantText;
    EditText reminderPlaceEditText;
    EditText dateEditText;

    AppCompatButton submitButton;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        findSection();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        plantType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlantDialog();
            }
        });
    }

    private void findSection() {
        backArrow = findViewById(R.id.backArrow);
        plantType = findViewById(R.id.reminderPlantLayout);



        reminderPlantText = findViewById(R.id.reminderPlantEditText);
        reminderPlaceEditText = findViewById(R.id.reminderPlaceEditText);
        dateEditText = findViewById(R.id.dateEditText);
        submitButton = findViewById(R.id.reminderSubmitButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void showPlantDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this,R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_plants, findViewById(R.id.plantDialogContainer));

        sheetView.findViewById(R.id.fruitLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                reminderPlantText.setText("Fruit");
            }
        });

        sheetView.findViewById(R.id.flowerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                reminderPlantText.setText("Flowers");
            }
        });

        sheetView.findViewById(R.id.medicineLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                reminderPlantText.setText("Medicine");
            }
        });

        sheetView.findViewById(R.id.economicalLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                reminderPlantText.setText("Economical");
            }
        });

        sheetView.findViewById(R.id.allLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                reminderPlantText.setText("All");
            }
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();

    }
}