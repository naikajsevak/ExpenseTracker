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

/**
 * RecyclerView Adapter to display a list of Category items.
 * Each item shows category name and icon with its background tint.
 * Provides a click listener interface to handle category item clicks.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolders> {

    private final Context context;
    private final ArrayList<Category> categories;
    private final CategoryClickListener categoryClickListener;

    /**
     * Interface to handle clicks on category items.
     */
    public interface CategoryClickListener {
        void onCategoryClick(Category category);
    }

    /**
     * Constructor for CategoryAdapter.
     *
     * @param context Context to access resources
     * @param categories List of categories to display
     * @param categoryClickListener Listener to handle category clicks
     */
    public CategoryAdapter(Context context, ArrayList<Category> categories, CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categories = categories;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single category item using view binding
        return new CategoryViewHolders(LayoutInflater.from(context).inflate(R.layout.sample_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolders holder, int position) {
        // Get current category
        Category category = categories.get(position);

        // Bind category name and icon
        holder.binding.categoryText.setText(category.getCategoryName());
        holder.binding.categoryIcon.setImageResource(category.getCategoryIcon());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));

        // Set click listener to notify when this category is clicked
        holder.itemView.setOnClickListener(view -> categoryClickListener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * ViewHolder class for category items using ViewBinding.
     */
    public static class CategoryViewHolders extends RecyclerView.ViewHolder {
        SampleCategoryItemBinding binding;

        public CategoryViewHolders(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}
