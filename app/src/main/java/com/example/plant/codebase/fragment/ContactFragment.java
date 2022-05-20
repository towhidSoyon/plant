package com.example.plant.codebase.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.codebase.adapter.ContactAdapter;
import com.example.plant.codebase.adapter.SliderAdapter;
import com.example.plant.codebase.model.ContactInfo;
import com.example.plant.codebase.model.SlideInfo;
import com.example.plant.codebase.utilities.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {

    SliderView sliderView;
    SliderAdapter adapter;

    private DatabaseReference SlideRef;

    RecyclerView contactRecyclerView;
    
    FirebaseFirestore firebaseFirestore;

    public ContactFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_contact, container, false);
        
        contactRecyclerView = view.findViewById(R.id.contactRecyclerView);
        
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        SlideRef = FirebaseDatabase.getInstance().getReference().child("ContactSlideShow");

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
        
        getContacts();
        
        return view;
    }

    private void getContacts() {

        firebaseFirestore.collection(Constants.KEY_CONTACTS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null){
                        List<ContactInfo> contactInfoList=new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            ContactInfo contacts=new ContactInfo();
                            contacts.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            contacts.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            contacts.phoneNumber = queryDocumentSnapshot.getString(Constants.KEY_PHONE_NUMBER);
                            contacts.district = queryDocumentSnapshot.getString(Constants.KEY_DISTRICT);
                            contactInfoList.add(contacts);
                        }
                        if (contactInfoList.size()>0){
                            ContactAdapter adapter=new ContactAdapter(contactInfoList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            contactRecyclerView.setAdapter(adapter);
                            contactRecyclerView.setLayoutManager(linearLayoutManager);
                        }
                        else {
                            showToast("Empty List");
                        }
                    }
                    else {
                        showToast(task.getException().getMessage().toString());
                    }
                });
    }

    public void showToast(String Message){
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
    }
}