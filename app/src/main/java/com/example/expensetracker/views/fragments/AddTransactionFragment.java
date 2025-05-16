package com.example.expensetracker.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensetracker.R;
import com.example.expensetracker.adapters.CategoryAdapter;
import com.example.expensetracker.databinding.FragmentAddTransactionBinding;
import com.example.expensetracker.databinding.ListDialogBinding;
import com.example.expensetracker.models.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddTransactionFragment extends Fragment {



    public AddTransactionFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentAddTransactionBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddTransactionBinding.inflate(inflater);
        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.text_color));
        });
        binding.expenseBtn.setOnClickListener(view -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.text_color));
        });

        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH,datePicker.getMonth());
                calendar.set(Calendar.YEAR,datePicker.getYear());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, YYYY");
                String dateToShow = simpleDateFormat.format(calendar.getTime());
                binding.date.setText(dateToShow);
            });
            datePickerDialog.show();
        });
        binding.category.setOnClickListener(view -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            ArrayList<Category> categories = new ArrayList<>();
            categories.add(new Category("Salary",R.drawable.wages,R.color.category1));
            categories.add(new Category("Business",R.drawable.briefcase,R.color.category2));
            categories.add(new Category("Investment",R.drawable.bar_chart,R.color.category3));
            categories.add(new Category("Loan",R.drawable.loan,R.color.category4));
            categories.add(new Category("Rent",R.drawable.key,R.color.category5));
            categories.add(new Category("Other",R.drawable.wallet,R.color.category6));

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),categories);
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);
            categoryDialog.show();
        });
        return inflater.inflate(R.layout.fragment_add_transaction, container, false);
    }
}