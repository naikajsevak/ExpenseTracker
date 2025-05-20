package com.example.expensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {
    public MutableLiveData<RealmResults<Transaction>> transaction = new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> total = new MutableLiveData<>();
    Realm realm;
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setUpDatabase();
    }
    void setUpDatabase()
    {
        realm = Realm.getDefaultInstance();
    }
    public void getTransaction(Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        RealmResults<Transaction> transaction =realm.where(Transaction.class).
        greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
        .findAll();

        double income = realm.where(Transaction.class).
                greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type",Constants.INCOME)
                .sum("amount").doubleValue();

        double expense = realm.where(Transaction.class).
                greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .equalTo("type",Constants.EXPENSE)
                .sum("amount").doubleValue();

        double total = realm.where(Transaction.class).
                greaterThanOrEqualTo("date",calendar.getTime())
                .lessThan("date",new Date(calendar.getTime().getTime()+(24*60*60*1000)))
                .sum("amount").doubleValue();

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        this.total.setValue(total);

        this.transaction.setValue(transaction);
    }
    public void addTransaction(Transaction transaction)
    {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
        realm.commitTransaction();
    }
    public void deleteTransaction()
    {
        realm.beginTransaction();
        realm.commitTransaction();
    }
}
