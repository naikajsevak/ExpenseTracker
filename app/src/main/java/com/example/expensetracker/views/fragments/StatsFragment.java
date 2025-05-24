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


public class StatsFragment extends Fragment {



    public StatsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentStatsBinding binding;
    Calendar calendar;
    public MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatsBinding.inflate(inflater);
        calendar = Calendar.getInstance();
        mainViewModel= new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        updateDate();
        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.text_color));
            Constants.SELECTED_STATS_TYPE =Constants.INCOME;
            updateDate();
        });
        binding.expenseBtn.setOnClickListener(view -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.text_color));
            Constants.SELECTED_STATS_TYPE =Constants.EXPENSE;
            updateDate();
        });
        binding.next.setOnClickListener(view -> {
            if(Constants.SELECTED_TAB_STATE==Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            }
            else if(Constants.SELECTED_TAB_STATE==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,1);
            }
            updateDate();
        });
        binding.back.setOnClickListener(view -> {
            if(Constants.SELECTED_TAB_STATE==Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }
            else if(Constants.SELECTED_TAB_STATE==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB_STATE=Constants.MONTHLY;
                    updateDate();
                } else if(tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB_STATE=Constants.DAILY;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        mainViewModel.categoryTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                if (transactions != null && !transactions.isEmpty()){
                   // Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                    binding.emptyState.setVisibility(View.GONE);
                    binding.anyChart.setVisibility(View.VISIBLE);
                    List<DataEntry> data = new ArrayList<>();
                    Map<String,Double> categoryMap= new HashMap<>();
                    for(Transaction transaction:transactions){
                        String category = transaction.getCategory();
                        double amount = transaction.getAmount();
                        if(categoryMap.containsKey(category)){
                            double currentTotal=categoryMap.get(category).doubleValue();
                            currentTotal+=Math.abs(amount);
                            categoryMap.put(category,currentTotal);
                        }else{
                            categoryMap.put(category,Math.abs(amount));
                        }
                    }
                    for(Map.Entry<String,Double> entry : categoryMap.entrySet()){
                        data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                    }
                    Pie pie = AnyChart.pie();
                    pie.data(data);
                    binding.anyChart.setChart(pie);
                }
                else{
                    binding.emptyState.setVisibility(View.VISIBLE);
                    binding.anyChart.setVisibility(View.GONE);
                }


            }
        });
        mainViewModel.getTransaction(calendar,Constants.SELECTED_STATS_TYPE);





        /*pie.title("Fruits imported in 2015 (in kg)");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);*/

        return binding.getRoot();
    }
    void updateDate()
    {
        if(Constants.SELECTED_TAB_STATE==Constants.DAILY){
            binding.currentDate.setText(Helper.formateDate(calendar.getTime()));
        }
        else if(Constants.SELECTED_TAB_STATE==Constants.MONTHLY){
            binding.currentDate.setText(Helper.formateDateByMonth(calendar.getTime()));
        }
        mainViewModel.getTransaction(calendar,Constants.SELECTED_STATS_TYPE);
    }
}