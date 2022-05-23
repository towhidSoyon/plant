package com.example.plant.codebase.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.plant.R;
import com.example.plant.codebase.activity.ChooseTreeActivity;
import com.example.plant.codebase.activity.ComplainActivity;
import com.example.plant.codebase.activity.MainActivity;
import com.example.plant.codebase.activity.ReminderActivity;
import com.example.plant.codebase.activity.SuggestedTreeActivity;
import com.example.plant.codebase.adapter.SliderAdapter;
import com.example.plant.codebase.model.SlideInfo;
import com.example.plant.codebase.network.WeatherApi;
import com.example.plant.codebase.network.WeatherApiService;
import com.example.plant.codebase.network.model.MyWeather;
import com.example.plant.codebase.network.model.Sys;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    SliderView sliderView;
    SliderAdapter adapter;

    private LinearLayout layoutChooseTree;
    private LinearLayout layoutSuggestTree;
    private LinearLayout layoutComplainDeforestation;
    private LinearLayout layoutNotification;
    private TextView temperature;
    private TextView weatherState;
    private ProgressBar progressBar;
    private TextView nameOfCity;

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
        progressBar = view.findViewById(R.id.weatherProgressLoading);

        weatherImage = view.findViewById(R.id.weatherImage);



        layoutChooseTree = view.findViewById(R.id.layout_choose_tree);
        layoutSuggestTree = view.findViewById(R.id.layout_suggest_tree);
        layoutComplainDeforestation = view.findViewById(R.id.layout_claim);
        layoutNotification = view.findViewById(R.id.layout_notification);



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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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


    }

    public  void  getWeatherResponse(){

       WeatherApiService apiService =  WeatherApi.getInstance().create(WeatherApiService.class);

       apiService.getApiResponse().enqueue(new Callback<MyWeather>() {
           @Override
           public void onResponse(Call<MyWeather> call, Response<MyWeather> response) {

               if (response!=null && response.isSuccessful()){


                   MyWeather myWeather = response.body();

                   nameOfCity.setText(myWeather.getName());
                   weatherState.setText(myWeather.getWeather().get(0).getMain());
                   temperature.setText(myWeather.getMain().getTemp()+" °C");

                   String iconName = myWeather.getWeather().get(0).getIcon();

                   String iconUrl ="https://openweathermap.org/img/wn/"+iconName+"@2x.png";
                   Log.v("Weather", "Icon Url "+ iconUrl);

                   Log.v("Weather" , myWeather.getName());


                   Glide.with(getContext())
                           .load(iconUrl)
                           .listener(new RequestListener<Drawable>() {
                               @Override
                               public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                   progressBar.setVisibility(View.VISIBLE);
                                   weatherImage.setVisibility(View.GONE);
                                   return false;
                               }

                               @Override
                               public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                   progressBar.setVisibility(View.GONE);
                                   weatherImage.setVisibility(View.VISIBLE);


                                   return false;
                               }
                           })

                           .into(weatherImage);


               }else {
                   System.out.println("Response Null");
               }

           }

           @Override
           public void onFailure(Call<MyWeather> call, Throwable t) {

               Log.v("Weather",t.getLocalizedMessage());
           }
       });


    }

    @Override
    public void onStart() {
        super.onStart();
        getWeatherResponse();
    }


    public void showToast(String Message) {
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
    }

}