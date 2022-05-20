package com.example.plant.codebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plant.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChooseLoginOrSignupActivity extends AppCompatActivity {

    Button loginButton;
    Button signupButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_or_signup);

        findSection();

        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginOrSignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginOrSignupActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(ChooseLoginOrSignupActivity.this, MainActivity.class));
            finish();
        }
    }

    private void findSection() {

        loginButton = findViewById(R.id.loginBtn);
        signupButton = findViewById(R.id.signupBtn);

    }
}