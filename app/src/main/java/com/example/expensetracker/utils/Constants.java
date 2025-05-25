package com.example.expensetracker.utils;

import com.example.expensetracker.R;
import com.example.expensetracker.models.Category;

import java.util.ArrayList;

/**
 * Utility class to hold app-wide constants and helper methods related to categories and accounts.
 */
public class Constants {

    // Constants to represent transaction types
    public static final String INCOME = "INCOME";
    public static final String EXPENSE = "EXPENSE";

    // UI state variables (used to track selected UI tabs and filters)
    public static int SELECTED_TAB = 0;
    public static int SELECTED_TAB_STATE = 0;
    public static String SELECTED_STATS_TYPE = Constants.INCOME;

    // Stats filter types
    public static final int DAILY = 0;
    public static final int MONTHLY = 1;
    public static final int CALENDER = 2;
    public static final int SUMMARY = 3;
    public static final int NOTES = 4;

    // List to store available categories (Income/Expense types)
    public static ArrayList<Category> categories;

    /**
     * Initializes and sets up the default list of categories with icons and colors.
     * Should be called once when app is initialized or when needed.
     */
    public static void setCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.wages, R.color.category1));
        categories.add(new Category("Business", R.drawable.briefcase, R.color.category2));
        categories.add(new Category("Investment", R.drawable.bar_chart, R.color.category3));
        categories.add(new Category("Loan", R.drawable.loan, R.color.category4));
        categories.add(new Category("Rent", R.drawable.key, R.color.category5));
        categories.add(new Category("Other", R.drawable.wallet, R.color.category6));
    }

    /**
     * Retrieves the details of a category based on its name.
     *
     * @param categoryName the name of the category to search for
     * @return Category object if found, otherwise null
     */
    public static Category getCategoryDetails(String categoryName) {
        for (Category cat : categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    /**
     * Returns a color resource based on the given account name.
     *
     * @param accountName the name of the account (e.g., Bank, Cash, Card)
     * @return the color resource ID corresponding to the account
     */
    public static int getAccountColor(String accountName) {
        switch (accountName) {
            case "Bank":
                return R.color.bank_color;
            case "Cash":
                return R.color.cash_color;
            case "Card":
                return R.color.card_color;
            default:
                return R.color.default_color;
        }
    }
}
