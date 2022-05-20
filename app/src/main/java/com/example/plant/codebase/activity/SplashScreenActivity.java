package com.example.plant.codebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.plant.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView logoImage;
    ImageView bgImage;

    Animation anim;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        findSection();
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        logoImage.startAnimation(anim);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               /* if (firebaseAuth.getCurrentUser() != null) {

                    Intent Intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(Intent);
                    finish();
                } else {*/
                    Intent Intent = new Intent(SplashScreenActivity.this, ChooseLoginOrSignupActivity.class);
                    startActivity(Intent);
                    finish();
                //}
            }
        }, 3000);
    }

    private void findSection() {
        logoImage = findViewById(R.id.logoImage);
        bgImage = findViewById(R.id.bgImage);
    }
}