package com.example.expensetracker.utils;

import com.example.expensetracker.R;
import com.example.expensetracker.models.Category;

import java.util.ArrayList;

public class Constants {
    public static final String INCOME="INCOME";
    public static final String EXPENSE="EXPENSE";
    public static int SELECTED_TAB=0;
    public static final int DAILY=0;
    public static final int MONTHLY=1;
    public static final int CALENDER=2;
    public static final int SUMMARY=3;
    public static final int NOTES=4;
    public static ArrayList<Category> categories;

    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.wages,R.color.category1));
        categories.add(new Category("Business",R.drawable.briefcase,R.color.category2));
        categories.add(new Category("Investment",R.drawable.bar_chart,R.color.category3));
        categories.add(new Category("Loan",R.drawable.loan,R.color.category4));
        categories.add(new Category("Rent",R.drawable.key,R.color.category5));
        categories.add(new Category("Other",R.drawable.wallet,R.color.category6));
    }
    public static Category getCategoryDetails(String categoryName){
        for (Category cat:categories) {
            if (cat.getCategoryName().equals(categoryName)){
                return cat;
            }
        }
        return null;
    }
    public static int getAccountColor(String accountName){
            switch (accountName){
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
