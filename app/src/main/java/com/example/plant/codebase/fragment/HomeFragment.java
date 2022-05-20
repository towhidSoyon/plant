package com.example.plant.codebase.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.codebase.activity.ChooseTreeActivity;
import com.example.plant.codebase.activity.ComplainActivity;
import com.example.plant.codebase.activity.MainActivity;
import com.example.plant.codebase.activity.ReminderActivity;
import com.example.plant.codebase.activity.SuggestedTreeActivity;
import com.example.plant.codebase.adapter.SliderAdapter;
import com.example.plant.codebase.model.SlideInfo;
import com.example.plant.codebase.utilities.CustomProgressDialog;
import com.example.plant.codebase.utilities.LanguageManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    SliderView sliderView;
    SliderAdapter adapter;

    private LinearLayout layoutChooseTree;
    private LinearLayout layoutSuggestTree;
    private LinearLayout layoutComplainDeforestation;
    private LinearLayout layoutNotification;
    private TextView temperature;
    private TextView weatherState;
    private TextView nameOfCity;

    private AppCompatButton languageBangla;
    private AppCompatButton languageEng;

    private ImageView weatherImage;

    private DatabaseReference SlideRef;

    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        temperature = view.findViewById(R.id.temperature);
        weatherState = view.findViewById(R.id.weatherState);
        nameOfCity = view.findViewById(R.id.nameOfCity);

        weatherImage = view.findViewById(R.id.weatherImage);

        languageBangla = view.findViewById(R.id.languageButtonBangla);
        languageEng = view.findViewById(R.id.languageButtonEng);

        layoutChooseTree = view.findViewById(R.id.layout_choose_tree);
        layoutSuggestTree = view.findViewById(R.id.layout_suggest_tree);
        layoutComplainDeforestation = view.findViewById(R.id.layout_claim);
        layoutNotification = view.findViewById(R.id.layout_notification);

        languageEng.setBackgroundResource(R.drawable.button_bg_7);

        LanguageManager languageManager = new LanguageManager(getContext());

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

        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ReminderActivity.class));
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