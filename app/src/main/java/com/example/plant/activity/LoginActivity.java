package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    ProgressBar progressBar;

    AppCompatButton loginButton;

    TextView bottomText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findSection();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailText.getText().toString();
                String password =passwordText.getText().toString();

                if (isValidSignUpDetails(email,password)){
                    loginUser(email,password);
                }
            }
        });

        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loginUser(String email, String password) {
        loading(true);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    loading(false);
                    showToast("Logged In");
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else {
                    loading(false);
                    showToast(task.getException().getMessage());
                }
            }
        });


    }

    private void findSection() {

        emailText = findViewById(R.id.loginEmail);
        passwordText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        progressBar =  findViewById(R.id.progressBar);
        bottomText = findViewById(R.id.forgotPassText);

    }

    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    public Boolean isValidSignUpDetails(String email, String password){
        if (email.trim().isEmpty()){
            showToast("Enter email");
            return false;
        }else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast("Enter Valid Email");
            return false;
        }else if (password.trim().isEmpty()){
            showToast("Enter Password");
            return false;
        }
        else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else{
            progressBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
}

