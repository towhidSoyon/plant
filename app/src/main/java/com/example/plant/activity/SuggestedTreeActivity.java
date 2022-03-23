package com.example.plant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.adapter.SuggestedTreeAdapter;
import com.example.plant.activity.model.SuggestedTree;
import com.example.plant.activity.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SuggestedTreeActivity extends AppCompatActivity implements SuggestedTreeAdapter.SuggestedTreeListener {

    private RecyclerView suggestedTreeRecyclerView;

    ImageView backArrow;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_tree);

        firebaseFirestore = FirebaseFirestore.getInstance();

        findSection();

        backArrow.setOnClickListener(view -> onBackPressed());

        getSuggestedTree();


    }

    private void getSuggestedTree() {

        firebaseFirestore.collection(Constants.KEY_COLLECTION_SUGGESTED_TREE)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null){
                        List<SuggestedTree> suggestedTreeList=new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            SuggestedTree tree=new SuggestedTree();
                            tree.suggestedTreeImage = queryDocumentSnapshot.getString(Constants.KEY_SUGGESTED_IMAGE);
                            tree.suggestedTreeTitle = queryDocumentSnapshot.getString(Constants.KEY_TITLE);
                            tree.suggestedTreeDesc = queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION);
                            suggestedTreeList.add(tree);
                        }
                        if (suggestedTreeList.size()>0){
                            SuggestedTreeAdapter adapter=new SuggestedTreeAdapter(suggestedTreeList,this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            suggestedTreeRecyclerView.setAdapter(adapter);
                            suggestedTreeRecyclerView.setLayoutManager(linearLayoutManager);
                        }
                        else {
                            showToast("Empty Suggestion");
                        }
                    }
                    else {
                        showToast(task.getException().getMessage().toString());
                    }
                });
    }

    private void findSection() {

        suggestedTreeRecyclerView = findViewById(R.id.suggestedTreeRecyclerView);
        backArrow = findViewById(R.id.backArrow);
    }


    public void showToast(String Message){
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuggestedTreeClicked(SuggestedTree suggestedTreeList) {
        Intent intent=new Intent(getApplicationContext(), SuggestedTreeDetailsActivity.class);
        intent.putExtra(Constants.KEY_COLLECTION_SUGGESTED_TREE, suggestedTreeList);
        startActivity(intent);
    }
}