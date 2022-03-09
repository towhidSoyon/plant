package com.example.plant.activity.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.adapter.ContactAdapter;
import com.example.plant.activity.model.ContactInfo;
import com.example.plant.activity.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {

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