package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;


import androidx.appcompat.app.AppCompatActivity;
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

    MainViewModel mainViewModel;
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