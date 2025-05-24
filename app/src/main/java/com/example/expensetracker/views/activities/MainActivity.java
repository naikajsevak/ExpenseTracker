package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.expensetracker.views.fragments.StatsFragment;
import com.example.expensetracker.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,new TransactionsFragment());
        transaction.commit();
        binding.bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId()==R.id.transaction)
                {
                    getSupportFragmentManager().popBackStack();
                }
                else if(item.getItemId()==R.id.stats){
                    transaction.replace(R.id.content,new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }
        });
    }
    public void getTransaction(){
        mainViewModel.getTransaction(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }
}