package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.expensetracker.R;
import com.example.expensetracker.adapters.TransactionsAdapter;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodel.MainViewModel;
import com.example.expensetracker.views.fragments.AddTransactionFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar calendar;
    public MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainViewModel= new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transaction");
        Constants.setCategories();
        calendar = Calendar.getInstance();
        updateDate();
        binding.next.setOnClickListener(view -> {
            if(Constants.SELECTED_TAB==Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            }
            else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,1);
            }
            updateDate();
        });
        binding.back.setOnClickListener(view -> {
            if(Constants.SELECTED_TAB==Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }
            else if(Constants.SELECTED_TAB==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });
        binding.floatingActionButton.setOnClickListener(view -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getSupportFragmentManager(), addTransactionFragment.getTag());
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB=1;
                    updateDate();
                } else if(tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB=0;
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
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        mainViewModel.transaction.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                    TransactionsAdapter adapter = new TransactionsAdapter(MainActivity.this, transactions);
                    binding.transactionList.setAdapter(adapter);
                    if(!transactions.isEmpty()) {
                        binding.emptyState.setVisibility(View.GONE);
                    }
                    else {
                        binding.emptyState.setVisibility(View.VISIBLE);
                    }
            }
        });
        mainViewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.income.setText(aDouble.toString());
            }
        });

        mainViewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expense.setText(aDouble.toString());
            }
        });

        mainViewModel.total.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble>=0) {
                    binding.totalAmount.setTextColor(getColor(R.color.green));
                }
                else {
                    binding.totalAmount.setTextColor(getColor(R.color.red));
                }
                binding.totalAmount.setText(aDouble.toString());
            }
        });
        mainViewModel.getTransaction(calendar);
    }
    public void getTransaction(){
        mainViewModel.getTransaction(calendar);
    }
    void updateDate()
    {
        if(Constants.SELECTED_TAB==Constants.DAILY){
            binding.currentDate.setText(Helper.formateDate(calendar.getTime()));
        }
        else if(Constants.SELECTED_TAB==Constants.MONTHLY){
            binding.currentDate.setText(Helper.formateDateByMonth(calendar.getTime()));
        }

        mainViewModel.getTransaction(calendar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }
}