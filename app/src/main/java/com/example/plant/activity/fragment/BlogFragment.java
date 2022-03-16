package com.example.plant.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.activity.AddBlogActivity;
import com.example.plant.activity.BlogDetailsActivity;
import com.example.plant.activity.adapter.BlogAdapter;
import com.example.plant.activity.adapter.ContactAdapter;
import com.example.plant.activity.model.BlogList;
import com.example.plant.activity.model.ContactInfo;
import com.example.plant.activity.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class BlogFragment extends Fragment implements BlogAdapter.BlogListener {

    private RecyclerView blogRecyclerView;

    private ImageView addBlog;

    FirebaseFirestore firebaseFirestore;

    public BlogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_blog, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        blogRecyclerView = view.findViewById(R.id.blogRecyclerView);


        addBlog = view.findViewById(R.id.imageAddBlog);

        addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddBlogActivity.class));

            }
        });

        getBlog();


        return view;
    }

    private void getBlog() {

        firebaseFirestore.collection(Constants.KEY_COLLECTION_BLOGS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null){
                        List<BlogList> blogLists=new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            BlogList blogs=new BlogList();
                            blogs.blogImage = queryDocumentSnapshot.getString(Constants.KEY_BLOG_IMAGE);
                            blogs.title = queryDocumentSnapshot.getString(Constants.KEY_TITLE);
                            blogs.author = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            blogs.description = queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION);
                            blogLists.add(blogs);
                        }
                        if (blogLists.size()>0){
                           BlogAdapter adapter=new BlogAdapter(blogLists,this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            blogRecyclerView.setAdapter(adapter);
                            blogRecyclerView.setLayoutManager(linearLayoutManager);
                        }
                        else {
                            showToast("Empty Blogs");
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


    @Override
    public void onBlogClicked(BlogList blogList) {
        Intent intent=new Intent(getContext(), BlogDetailsActivity.class);
        intent.putExtra(Constants.KEY_BLOG, blogList);
        startActivity(intent);
    }
}