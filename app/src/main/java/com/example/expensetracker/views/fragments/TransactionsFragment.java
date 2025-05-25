package com.example.expensetracker.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensetracker.R;
import com.example.expensetracker.adapters.TransactionsAdapter;
import com.example.expensetracker.databinding.FragmentTransactionsBinding;
import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;

/**
 * Fragment responsible for displaying transaction records in different views (Daily, Monthly).
 * It also shows the total income, expense, and balance, and allows adding new transactions.
 */
public class TransactionsFragment extends Fragment {

    // View binding instance for accessing views in the fragment layout
    FragmentTransactionsBinding binding;

    // Calendar instance to keep track of the selected date or month for filtering transactions
    Calendar calendar;

    // ViewModel instance shared with the activity to observe and fetch transaction data
    public MainViewModel mainViewModel;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No special initialization needed here for now
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout using ViewBinding for type-safe view references
        binding = FragmentTransactionsBinding.inflate(inflater);

        // Initialize calendar instance with current date/time
        calendar = Calendar.getInstance();

        // Obtain the shared MainViewModel instance from the hosting Activity
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Display the current date/month based on the selected tab and load transactions accordingly
        updateDate();

        // Setup click listener for 'Next' button to increment date/month filter
        binding.next.setOnClickListener(view -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);  // Next day
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1); // Next month
            }
            updateDate();
        });

        // Setup click listener for 'Back' button to decrement date/month filter
        binding.back.setOnClickListener(view -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1); // Previous day
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1); // Previous month
            }
            updateDate();
        });

        // Floating Action Button opens a dialog to add a new transaction
        binding.floatingActionButton.setOnClickListener(view -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getParentFragmentManager(), addTransactionFragment.getTag());
        });

        // TabLayout listener to switch between Daily and Monthly transaction views
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Update selected tab constant and refresh displayed transactions accordingly
                if (tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = Constants.MONTHLY;
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = Constants.DAILY;
                }
                updateDate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed on unselect
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed on reselect
            }
        });

        // Set up RecyclerView with a linear vertical layout manager for displaying transactions
        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe transaction data changes and update RecyclerView adapter accordingly
        mainViewModel.transaction.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactions);
                binding.transactionList.setAdapter(adapter);

                // Show or hide empty state placeholder based on data availability
                if (!transactions.isEmpty()) {
                    binding.emptyState.setVisibility(View.GONE);
                } else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        // Observe and display the total income amount
        mainViewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalIncome) {
                binding.income.setText(String.valueOf(totalIncome));
            }
        });

        // Observe and display the total expense amount
        mainViewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalExpense) {
                binding.expense.setText(String.valueOf(totalExpense));
            }
        });

        // Observe and display the net total amount; color code based on positive or negative value
        mainViewModel.total.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                if (total >= 0) {
                    binding.totalAmount.setTextColor(getContext().getColor(R.color.green));
                } else {
                    binding.totalAmount.setTextColor(getContext().getColor(R.color.red));
                }
                binding.totalAmount.setText(String.valueOf(total));
            }
        });

        // Initial fetch of transactions filtered by the current date or month
        mainViewModel.getTransaction(calendar);

        return binding.getRoot();
    }

    /**
     * Updates the displayed date or month text based on the selected tab and
     * triggers the ViewModel to fetch the relevant transactions.
     */
    void updateDate() {
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            // Format and display the date for daily view
            binding.currentDate.setText(Helper.formateDate(calendar.getTime()));
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            // Format and display the month for monthly view
            binding.currentDate.setText(Helper.formateDateByMonth(calendar.getTime()));
        }
        // Fetch transactions for the updated date/month filter
        mainViewModel.getTransaction(calendar);
    }
}
