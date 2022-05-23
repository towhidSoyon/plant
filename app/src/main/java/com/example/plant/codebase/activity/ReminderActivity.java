package com.example.plant.codebase.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.codebase.utilities.ReminderBroadcast;
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

        backArrow.setOnClickListener(view -> onBackPressed());

        plantType.setOnClickListener(view -> showPlantDialog());

        createNotificationChannel();

        submitButton.setOnClickListener(view -> {
            String plant = reminderPlantText.getText().toString();
            String place = reminderPlaceEditText.getText().toString();
            String date = dateEditText.getText().toString();

            if (isValidDetails(plant,place,date)){

                showToast("You will get notifications to give water to plants!");
                Intent intent = new Intent(ReminderActivity.this, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this,0,intent,0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick= System.currentTimeMillis();

                long triggeringSecondsInMillis = 1000*60;

                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick+triggeringSecondsInMillis,pendingIntent);
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

    @SuppressLint("SetTextI18n")
    private void showPlantDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this,R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_plants, findViewById(R.id.plantDialogContainer));

        sheetView.findViewById(R.id.fruitLayout).setOnClickListener(view -> {
            sheetDialog.dismiss();
            reminderPlantText.setText("Fruit");
        });

        sheetView.findViewById(R.id.flowerLayout).setOnClickListener(view -> {
            sheetDialog.dismiss();
            reminderPlantText.setText("Flowers");
        });

        sheetView.findViewById(R.id.medicineLayout).setOnClickListener(view -> {
            sheetDialog.dismiss();
            reminderPlantText.setText("Medicine");
        });

        sheetView.findViewById(R.id.economicalLayout).setOnClickListener(view -> {
            sheetDialog.dismiss();
            reminderPlantText.setText("Economical");
        });

        sheetView.findViewById(R.id.allLayout).setOnClickListener(view -> {
            sheetDialog.dismiss();
            reminderPlantText.setText("All");
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();

    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "PlantReminderChannel";
            String description = "Channel for Plant Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyPlant",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Boolean isValidDetails(String plant, String location,String date){
        if (plant.trim().isEmpty()){
            showToast("Enter Types of Plant");
            return false;
        }else if ( location.trim().isEmpty()){
            showToast("Enter Valid Location");
            return false;
        }else if (date.trim().isEmpty()){
            showToast("Enter Date");
            return false;
        }
        else {
            return true;
        }
    }

    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }


}