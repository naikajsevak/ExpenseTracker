package com.example.expensetracker.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.FragmentStatsBinding;
import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Fragment responsible for displaying statistics of transactions in a Pie Chart.
 * Supports filtering by Income or Expense and viewing data in Daily or Monthly mode.
 */
public class StatsFragment extends Fragment {

    // View binding instance for accessing fragment views safely
    FragmentStatsBinding binding;

    // Calendar instance to track the currently selected date or month
    Calendar calendar;

    // Shared ViewModel instance to fetch and observe transaction data
    public MainViewModel mainViewModel;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No special initialization required here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout using ViewBinding
        binding = FragmentStatsBinding.inflate(inflater);

        // Initialize calendar with current date/time
        calendar = Calendar.getInstance();

        // Obtain the shared ViewModel scoped to the activity
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Set initial date and load data accordingly
        updateDate();

        // Setup income button click listener to filter stats by income transactions
        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.text_color));
            Constants.SELECTED_STATS_TYPE = Constants.INCOME;
            updateDate();
        });

        // Setup expense button click listener to filter stats by expense transactions
        binding.expenseBtn.setOnClickListener(view -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.text_color));
            Constants.SELECTED_STATS_TYPE = Constants.EXPENSE;
            updateDate();
        });

        // Next button to increment date or month filter
        binding.next.setOnClickListener(view -> {
            if (Constants.SELECTED_TAB_STATE == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);  // Next day
            } else if (Constants.SELECTED_TAB_STATE == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1); // Next month
            }
            updateDate();
        });

        // Back button to decrement date or month filter
        binding.back.setOnClickListener(view -> {
            if (Constants.SELECTED_TAB_STATE == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1); // Previous day
            } else if (Constants.SELECTED_TAB_STATE == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1); // Previous month
            }
            updateDate();
        });

        // TabLayout listener to switch between Daily and Monthly statistics views
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB_STATE = Constants.MONTHLY;
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB_STATE = Constants.DAILY;
                }
                updateDate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed here
            }
        });

        // Observe transactions filtered by category for the selected date/month and type (income/expense)
        mainViewModel.categoryTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                Pie pie = AnyChart.pie();

                if (transactions != null && !transactions.isEmpty()) {
                    // Show pie chart and hide empty state if data is available
                    binding.emptyState.setVisibility(View.GONE);
                    binding.anyChart.setVisibility(View.VISIBLE);

                    // Prepare data for pie chart by summing amounts per category
                    List<DataEntry> data = new ArrayList<>();
                    Map<String, Double> categoryMap = new HashMap<>();

                    for (Transaction transaction : transactions) {
                        String category = transaction.getCategory();
                        double amount = Math.abs(transaction.getAmount());

                        // Aggregate amount by category
                        categoryMap.put(category, categoryMap.getOrDefault(category, 0.0) + amount);
                    }

                    // Convert map entries to pie chart data entries
                    for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
                        data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                    }

                    // Set data and display pie chart
                    pie.data(data);
                    binding.anyChart.setChart(pie);
                } else {
                    // Show empty state and hide pie chart if no data
                    binding.emptyState.setVisibility(View.VISIBLE);
                    binding.anyChart.setVisibility(View.GONE);
                }
            }
        });

        // Initial load of transactions based on current date/month and selected stats type
        mainViewModel.getTransaction(calendar, Constants.SELECTED_STATS_TYPE);

        return binding.getRoot();
    }

    /**
     * Updates the displayed date/month text and triggers data fetch
     * based on the currently selected date, month, and stats type.
     */
    void updateDate() {
        if (Constants.SELECTED_TAB_STATE == Constants.DAILY) {
            binding.currentDate.setText(Helper.formateDate(calendar.getTime()));
        } else if (Constants.SELECTED_TAB_STATE == Constants.MONTHLY) {
            binding.currentDate.setText(Helper.formateDateByMonth(calendar.getTime()));
        }
        mainViewModel.getTransaction(calendar, Constants.SELECTED_STATS_TYPE);
    }
}
