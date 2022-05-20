package com.example.plant.codebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant.R;
import com.example.plant.codebase.activity.SuggestedTreeActivity;
import com.example.plant.codebase.model.SuggestedTree;

import java.util.List;

public class SuggestedTreeAdapter extends RecyclerView.Adapter<SuggestedTreeAdapter.ViewHolder> {

    List<SuggestedTree> listSuggestedTree;
    private SuggestedTreeListener suggestedTreeListener;

    public SuggestedTreeAdapter(List<SuggestedTree> listSuggestedTree, SuggestedTreeActivity suggestedTreeActivity) {
        this.listSuggestedTree = listSuggestedTree;
    }

    @NonNull
    @Override
    public SuggestedTreeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_tree_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedTreeAdapter.ViewHolder holder, int position) {
        holder.setSuggestedTreeData(listSuggestedTree.get(position));
    }

    @Override
    public int getItemCount() {
        return listSuggestedTree.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView suggestedTreeText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestedTreeText = itemView.findViewById(R.id.suggestedTreeText);
        }

        void setSuggestedTreeData(SuggestedTree suggestedTree){
            suggestedTreeText.setText(suggestedTree.suggestedTreeTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    suggestedTreeListener.onSuggestedTreeClicked(suggestedTree);
                }
            });
        }
    }
    public interface SuggestedTreeListener {
        void onSuggestedTreeClicked(SuggestedTree suggestedTreeList);
    }
}
