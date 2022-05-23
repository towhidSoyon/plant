package com.example.plant.codebase.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.plant.R;
import com.example.plant.codebase.utilities.LanguageManager;

public class SettingsActivity extends AppCompatActivity {

    private AppCompatButton languageBangla;
    private AppCompatButton languageEng;

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageBangla = findViewById(R.id.languageButtonBangla);
        languageEng = findViewById(R.id.languageButtonEng);
        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LanguageManager languageManager = new LanguageManager(this);

        languageEng.setBackgroundResource(R.drawable.button_bg_7);


        languageBangla.setOnClickListener(view1 -> {
            languageBangla.setBackgroundResource(R.drawable.button_bg_6);
            languageEng.setBackgroundResource(R.drawable.button_bg_5);

            languageManager.updateResource("bn");

        });

        languageEng.setOnClickListener(view12 -> {
            languageEng.setBackgroundResource(R.drawable.button_bg_7);
            languageBangla.setBackgroundResource(R.drawable.button_bg_4);

            languageManager.updateResource("en");

        });
    }

    public void updateLanguageState(){
        LanguageManager languageManager = new LanguageManager(this);

        if (languageManager.getLang().equals("en")){
            languageEng.setBackgroundResource(R.drawable.button_bg_7);
            languageBangla.setBackgroundResource(R.drawable.button_bg_4);
        }else {

            languageBangla.setBackgroundResource(R.drawable.button_bg_6);
            languageEng.setBackgroundResource(R.drawable.button_bg_5);

        }
    }
}