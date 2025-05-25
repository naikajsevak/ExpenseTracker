package com.example.expensetracker.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.viewmodel.MainViewModel;
import com.example.expensetracker.views.fragments.StatsFragment;
import com.example.expensetracker.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

/**
 * MainActivity serves as the entry point for the application UI.
 * It handles the setup of the toolbar, navigation between fragments,
 * and communication with the ViewModel to manage transaction data.
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding; // ViewBinding object to access UI components
    Calendar calendar;           // Calendar instance for tracking current date
    public MainViewModel mainViewModel; // ViewModel for data handling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transaction");

        // Initialize category data
        Constants.setCategories();

        // Initialize current calendar instance
        calendar = Calendar.getInstance();

        // Set the default fragment (TransactionsFragment)
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragment());
        transaction.commit();

        // Handle bottom navigation item selection
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId() == R.id.transaction) {
                    // Pop back stack to show TransactionsFragment
                    getSupportFragmentManager().popBackStack();
                } else if (item.getItemId() == R.id.stats) {
                    // Navigate to StatsFragment
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });
    }

    /**
     * Triggers the ViewModel to fetch transaction data based on the current calendar date.
     */
    public void getTransaction(){
        mainViewModel.getTransaction(calendar);
    }

    /**
     * Inflates the top options menu into the toolbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
