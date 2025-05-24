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
import com.example.expensetracker.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;


public class TransactionsFragment extends Fragment {



    public TransactionsFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentTransactionsBinding binding;
    Calendar calendar;
    public MainViewModel mainViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentTransactionsBinding.inflate(inflater);
        calendar = Calendar.getInstance();
        mainViewModel= new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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
            addTransactionFragment.show(getParentFragmentManager(), addTransactionFragment.getTag());
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
        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));
        mainViewModel.transaction.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), transactions);
                binding.transactionList.setAdapter(adapter);
                if(!transactions.isEmpty()) {
                    binding.emptyState.setVisibility(View.GONE);
                }
                else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });
        mainViewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.income.setText(aDouble.toString());
            }
        });

        mainViewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expense.setText(aDouble.toString());
            }
        });

        mainViewModel.total.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if(aDouble>=0) {
                    binding.totalAmount.setTextColor(getContext().getColor(R.color.green));
                }
                else {
                    binding.totalAmount.setTextColor(getContext().getColor(R.color.red));
                }
                binding.totalAmount.setText(aDouble.toString());
            }
        });
        mainViewModel.getTransaction(calendar);

        return binding.getRoot();
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
}