package com.example.expensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {
    public MutableLiveData<RealmResults<Transaction>> transaction = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoryTransactions = new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> total = new MutableLiveData<>();
    Realm realm;
    Calendar calendar;
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setUpDatabase();
    }
    void setUpDatabase()
    {
        realm = Realm.getDefaultInstance();
    }
    public void getTransaction(Calendar inputCalendar, String type) {
        // Use the passed-in calendar instead of overwriting
        Calendar calendar = (Calendar) inputCalendar.clone();

        RealmResults<Transaction> transaction = null;

        if (Constants.SELECTED_TAB_STATE == Constants.DAILY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date start = calendar.getTime();
            Date end = new Date(start.getTime() + 24 * 60 * 60 * 1000);

            transaction = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", start)
                    .lessThan("date", end)
                    .equalTo("type", type)
                    .findAll();

        } else if (Constants.SELECTED_TAB_STATE == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            Date endTime = calendar.getTime();

            transaction = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", type)
                    .findAll();
        }
        categoryTransactions.setValue(transaction);

    }

    public void getTransaction(Calendar calendar)
    {
        this. calendar = Calendar.getInstance();
        double income=0,expense=0,total=0;
        RealmResults<Transaction> transaction=null;
        if(Constants.SELECTED_TAB ==Constants.DAILY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            transaction = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .findAll();

             income = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

             expense = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

             total = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount").doubleValue();
        }
        else if(Constants.SELECTED_TAB==Constants.MONTHLY){
            calendar.set(Calendar.DAY_OF_MONTH,0);
            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();
            transaction = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date",startTime)
                    .lessThan("date", endTime)
                    .findAll();
            income = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total = realm.where(Transaction.class).
                    greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000)))
                    .sum("amount").doubleValue();
        }
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
    public void deleteTransaction(Transaction transaction)
    {
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransaction(calendar);
    }
}
