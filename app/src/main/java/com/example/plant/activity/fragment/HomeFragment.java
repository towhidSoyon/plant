package com.example.plant.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plant.R;
import com.example.plant.activity.ChooseTreeActivity;
import com.example.plant.activity.ComplainActivity;
import com.example.plant.activity.MainActivity;
import com.example.plant.activity.SuggestedTreeActivity;
import com.example.plant.activity.adapter.SliderAdapter;
import com.example.plant.activity.model.SlideInfo;
import com.example.plant.activity.model.WeatherData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    SliderView sliderView;
    SliderAdapter adapter;

    private LinearLayout layoutChooseTree;
    private LinearLayout layoutSuggestTree;
    private LinearLayout layoutComplainDeforestation;
    private TextView temperature;
    private TextView weatherState;
    private TextView nameOfCity;

    private ImageView weatherImage;

    private DatabaseReference SlideRef;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        temperature = view.findViewById(R.id.temperature);
        weatherState = view.findViewById(R.id.weatherState);
        nameOfCity = view.findViewById(R.id.nameOfCity);

        weatherImage = view.findViewById(R.id.weatherImage);


        layoutChooseTree = view.findViewById(R.id.layout_choose_tree);
        layoutSuggestTree = view.findViewById(R.id.layout_suggest_tree);
        layoutComplainDeforestation = view.findViewById(R.id.layout_claim);

        layoutChooseTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChooseTreeActivity.class));
            }
        });

        layoutSuggestTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SuggestedTreeActivity.class));
            }
        });

        layoutComplainDeforestation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ComplainActivity.class));
            }
        });

        sliderView = view.findViewById(R.id.imageSlider);
        adapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(R.color.colorPrimary);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(6); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        final List<SlideInfo> sliderItemList = new ArrayList<>();

        SlideRef = FirebaseDatabase.getInstance().getReference().child("SlideShow");

        SlideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        SlideInfo posts = postSnapshot.getValue(SlideInfo.class);

                        String key = postSnapshot.getKey();
                        String description = snapshot.child(key).child("description").getValue().toString();
                        String url = snapshot.child(key).child("imageUrl").getValue().toString();

                        posts.setImageKey(key);
                        posts.setImageUrl(url);
                        posts.setDescription(description);

                        sliderItemList.add(posts);

                    }
                    adapter.renewItems((ArrayList<SlideInfo>) sliderItemList);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public void showToast(String Message) {
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
    }

}