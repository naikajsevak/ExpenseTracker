package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;


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
            calendar.add(Calendar.DATE,1);
            updateDate();
        });
        binding.back.setOnClickListener(view -> {
            calendar.add(Calendar.DATE,-1);
            updateDate();
        });
        binding.floatingActionButton.setOnClickListener(view -> {
            AddTransactionFragment addTransactionFragment = new AddTransactionFragment();
            addTransactionFragment.show(getSupportFragmentManager(), addTransactionFragment.getTag());
        });
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        mainViewModel.transaction.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                    TransactionsAdapter adapter = new TransactionsAdapter(MainActivity.this, transactions);
                    binding.transactionList.setAdapter(adapter);
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
        binding.currentDate.setText(Helper.formateDate(calendar.getTime()));
        mainViewModel.getTransaction(calendar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }
}