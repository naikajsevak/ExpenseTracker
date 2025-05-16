package com.example.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.SampleCategoryItemBinding;
import com.example.expensetracker.models.Category;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolders> {
    Context context;
    ArrayList<Category> categories;
    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.context=context;
        this.categories=categories;
    }
    @NonNull
    @Override
    public CategoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolders(LayoutInflater.from(context).inflate(R.layout.sample_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolders holder, int position) {
        Category category = categories.get(position);
        holder.binding.categoryText.setText(category.getCategoryName());
        holder.binding.categoryIcon.setImageResource(category.getCategoryIcon());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolders extends RecyclerView.ViewHolder{
        SampleCategoryItemBinding binding;
        public CategoryViewHolders(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}
