package com.example.expensetracker.models;

/**
 * Model class representing a Category.
 * Each category has a name, an associated icon resource, and a color.
 * Used for categorizing transactions in the expense tracker.
 */
public class Category {

    private String categoryName;  // Name of the category (e.g., Food, Transport)
    private int categoryIcon;     // Drawable resource ID for the category icon
    private int categoryColor;    // Color resource ID associated with the category

    /**
     * Default no-argument constructor.
     * Required for serialization or frameworks that instantiate the class reflectively.
     */
    public Category() {
    }

    /**
     * Parameterized constructor to create a category instance with specified attributes.
     *
     * @param categoryName Name of the category
     * @param categoryIcon Drawable resource ID for the icon
     * @param categoryColor Color resource ID associated with the category
     */
    public Category(String categoryName, int categoryIcon, int categoryColor) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryColor = categoryColor;
    }

    /**
     * Getter for category name.
     * @return The name of the category
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Setter for category name.
     * @param categoryName The new name of the category
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Getter for the icon resource ID.
     * @return Drawable resource ID representing the icon of the category
     */
    public int getCategoryIcon() {
        return categoryIcon;
    }

    /**
     * Setter for the icon resource ID.
     * @param categoryIcon Drawable resource ID to set for the category icon
     */
    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    /**
     * Getter for the color resource ID.
     * @return Color resource ID associated with the category
     */
    public int getCategoryColor() {
        return categoryColor;
    }

    /**
     * Setter for the color resource ID.
     * @param categoryColor Color resource ID to set for the category
     */
    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }
}
