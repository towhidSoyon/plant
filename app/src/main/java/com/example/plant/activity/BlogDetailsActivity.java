package com.example.plant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plant.R;
import com.example.plant.activity.model.BlogList;
import com.example.plant.activity.utilities.Constants;
import com.squareup.picasso.Picasso;

public class BlogDetailsActivity extends AppCompatActivity {

    ImageView backArrow;
    ImageView blogDetailsImage;

    TextView blogDetailsTitle;
    TextView authorName;
    TextView publishedDate;

    TextView blogDesc;

    BlogList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        findSection();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        list = (BlogList) getIntent().getSerializableExtra(Constants.KEY_BLOG);

        blogDetailsTitle.setText(list.title);
        publishedDate.setText(list.date);
        Picasso.get().load(list.blogImage).fit().centerInside().placeholder(R.drawable.placeholder_image).
                into(blogDetailsImage);
        authorName.setText(list.author);
        blogDesc.setText(list.description);




    }

    private void findSection() {

        backArrow = findViewById(R.id.backArrow);
        blogDetailsTitle = findViewById(R.id.suggestedTreeDetailsTitle);
        blogDetailsImage = findViewById(R.id.suggestedTreeDetailsImage);
        authorName = findViewById(R.id.blogDetailsAuthorName);
        publishedDate = findViewById(R.id.blogDetailsDate);
        blogDesc = findViewById(R.id.suggestedTreeDetailsDesc);
    }
}