
package com.example.plant.activity.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant.R;
import com.example.plant.activity.model.BlogList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private List<BlogList> blogLists;
    private BlogListener blogListener;

    public BlogAdapter(List<BlogList> blogLists, BlogListener blogListener) {
        this.blogLists = blogLists;
        this.blogListener = blogListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setBlogData(blogLists.get(position));

    }

    @Override
    public int getItemCount() {
        return blogLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView blogListImage;
        TextView blogListTitle;
        TextView blogListAuthor;
        TextView blogListDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            blogListImage = itemView.findViewById(R.id.listBlogImage);
            blogListTitle = itemView.findViewById(R.id.listBlogTitle);
            blogListAuthor = itemView.findViewById(R.id.listAuthor);
            blogListDesc = itemView.findViewById(R.id.listBlogDesc);

        }

        void setBlogData(BlogList blog){

            Picasso.get().load(blog.blogImage).fit().centerInside().placeholder(R.drawable.placeholder_image).
                    into(blogListImage);
            blogListTitle.setText(blog.title);
            blogListAuthor.setText(blog.author);
            blogListDesc.setText(blog.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    blogListener.onBlogClicked(blog);
                }
            });

        }

        /*private Bitmap getBlogImage(String encodedImage){
            byte[] bytes= Base64.decode(encodedImage,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        }*/
    }

    public interface BlogListener {
        void onBlogClicked(BlogList blogList);
    }
}
