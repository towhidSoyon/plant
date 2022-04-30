package com.example.plant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.plant.R;
import com.example.plant.activity.fragment.AboutFragment;
import com.example.plant.activity.fragment.BlogFragment;
import com.example.plant.activity.fragment.ContactFragment;
import com.example.plant.activity.fragment.HomeFragment;
import com.example.plant.activity.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    private CircleImageView profileImage;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        /*if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),ChooseLoginOrSignupActivity.class));
            finish();
        }*/



        initToolbar();

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        View navView =  navigationView.inflateHeaderView(R.layout.nav_header_main);

        CircleImageView headerImage =  navView.findViewById(R.id.navDrawerImage);
        TextView headerName = navView.findViewById(R.id.navDrawerNameText);
        TextView headerAddress = navView.findViewById(R.id.navDrawerAddress);

        String cUser = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(cUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String img = task.getResult().get("image").toString();
                    /*byte[] bytes = Base64.decode(img, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    headerImage.setImageBitmap(bitmap);*/
                    Picasso.get().load(img).into(headerImage);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(cUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().get("name").toString();
                    headerName.setText(userName);
                }
            }
        });

        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(cUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userAddress = task.getResult().get("location").toString();
                    headerAddress.setText(userAddress);
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            Fragment temp;

            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        temp = new HomeFragment();
                        break;
                    case R.id.nav_blog:
                        temp = new BlogFragment();
                        break;
                    case R.id.nav_contacts:
                        temp = new ContactFragment();
                        break;
                    case R.id.nav_about:
                        temp = new AboutFragment();
                        break;
                    case R.id.nav_logout:
                        firebaseAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),ChooseLoginOrSignupActivity.class));
                        finish();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    private void initToolbar() {


        String currentUser = firebaseAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        profileImage = findViewById(R.id.profileImage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseFirestore.collection(Constants.KEY_COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String img = task.getResult().get("image").toString();
                    /*byte[] bytes = Base64.decode(img, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profileImage.setImageBitmap(bitmap);*/
                    Picasso.get().load(img).into(profileImage);
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    public void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialouge_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout viewProfile = dialog.findViewById(R.id.viewProfile);
        LinearLayout editProfile = dialog.findViewById(R.id.editProfile);


        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
            }
        });

        dialog.show();
    }
}