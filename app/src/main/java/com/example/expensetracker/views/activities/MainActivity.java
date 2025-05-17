package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.expensetracker.R;
import com.example.expensetracker.adapters.TransactionsAdapter;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.views.fragments.AddTransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        transactionArrayList.add(new Transaction(Constants.INCOME,"Business","Card","Business related",new Date(),500,1));
        TransactionsAdapter adapter = new TransactionsAdapter(this,transactionArrayList);
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionList.setAdapter(adapter);
    }
    void setUpDatabase()
    {
        Real
    }
    void updateDate()
    {
        binding.currentDate.setText(Helper.formateDate(calendar.getTime()));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }
}