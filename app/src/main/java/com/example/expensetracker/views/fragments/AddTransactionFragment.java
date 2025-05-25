package com.example.expensetracker.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensetracker.R;
import com.example.expensetracker.adapters.AccountAdapter;
import com.example.expensetracker.adapters.CategoryAdapter;
import com.example.expensetracker.databinding.FragmentAddTransactionBinding;
import com.example.expensetracker.databinding.ListDialogBinding;
import com.example.expensetracker.models.Accounts;
import com.example.expensetracker.models.Category;
import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodel.MainViewModel;
import com.example.expensetracker.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Bottom sheet fragment to add a new transaction (Income/Expense).
 * This fragment allows the user to select transaction type, date, amount, category, and account.
 */
public class AddTransactionFragment extends BottomSheetDialogFragment {

    // Model to hold transaction details
    Transaction transaction;

    public AddTransactionFragment() {
        // Default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransactionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize a new transaction instance
        transaction = new Transaction();

        // Inflate view using ViewBinding
        binding = FragmentAddTransactionBinding.inflate(inflater);

        // Handle Income button click
        binding.incomeBtn.setOnClickListener(view -> {
            // Update button styles to reflect selection
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.text_color));

            // Set transaction type to income
            transaction.setType(Constants.INCOME);
        });

        // Handle Expense button click
        binding.expenseBtn.setOnClickListener(view -> {
            // Update button styles to reflect selection
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.text_color));

            // Set transaction type to expense
            transaction.setType(Constants.EXPENSE);
        });

        // Handle date picker dialog
        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);

                // Display selected date and update transaction
                binding.date.setText(Helper.formateDate(calendar.getTime()));
                transaction.setDate(calendar.getTime());
                transaction.setId(calendar.getTime().getTime());
                datePickerDialog.show();
            });
            datePickerDialog.show();
        });

        // Handle Save Transaction button click
        binding.saveTransactionBtn.setOnClickListener(view -> {
            double amount = Double.parseDouble(binding.amt.getText().toString());
            String note  = binding.note.getText().toString();

            // Set amount negative for expense, positive for income
            if (transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount * -1);
            } else {
                transaction.setAmount(amount);
            }

            // Add note to transaction
            transaction.setNote(note);

            // Save transaction using ViewModel
            ((MainActivity) getActivity()).mainViewModel.addTransaction(transaction);
            ((MainActivity) getActivity()).getTransaction();

            // Close the bottom sheet
            dismiss();
        });

        // Handle Category selection dialog
        binding.category.setOnClickListener(view -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());

            // Set up adapter for category list
            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, category -> {
                binding.category.setText(category.getCategoryName());
                transaction.setCategory(category.getCategoryName());
                categoryDialog.dismiss();
            });

            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);
            categoryDialog.show();
        });

        // Handle Account selection dialog
        binding.act.setOnClickListener(view -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(dialogBinding.getRoot());

            // Create list of available accounts
            ArrayList<Accounts> accountsArrayList = new ArrayList<>();
            accountsArrayList.add(new Accounts(0, "Cash"));
            accountsArrayList.add(new Accounts(0, "Bank"));
            accountsArrayList.add(new Accounts(0, "Gpay"));
            accountsArrayList.add(new Accounts(0, "Paytm"));
            accountsArrayList.add(new Accounts(0, "BHIMUPI"));
            accountsArrayList.add(new Accounts(0, "Other"));

            // Set up adapter for account list
            AccountAdapter accountAdapter = new AccountAdapter(getContext(), accountsArrayList, accounts -> {
                binding.act.setText(accounts.getAccountName());
                transaction.setAccount(accounts.getAccountName());
                accountDialog.dismiss();
            });

            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(accountAdapter);
            accountDialog.show();
        });

        return binding.getRoot();
    }
}
