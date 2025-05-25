package com.example.expensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.util.UnstableApi;

import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
/**
 * ViewModel for managing transaction-related data and operations in the Expense Tracker app.
 * This class uses Realm as the local database and exposes LiveData for UI components.
 */
public class MainViewModel extends AndroidViewModel {

    // LiveData objects for observing data changes
    public MutableLiveData<RealmResults<Transaction>> transaction = new MutableLiveData<>();
    public MutableLiveData<RealmResults<Transaction>> categoryTransactions = new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> total = new MutableLiveData<>();

    Realm realm;
    Calendar calendar;

    /**
     * Constructor - initializes Realm database
     */
    @OptIn(markerClass = UnstableApi.class)
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setUpDatabase();
    }

    /**
     * Initializes the Realm database instance
     */
    void setUpDatabase() {
        realm = Realm.getDefaultInstance();
    }

    /**
     * Fetches category-specific transactions based on selected tab state (DAILY or MONTHLY)
     * and transaction type (INCOME or EXPENSE).
     */
    public void getTransaction(Calendar inputCalendar, String type) {
        Calendar calendar = (Calendar) inputCalendar.clone(); // Clone input to avoid modifying original
        RealmResults<Transaction> transaction = null;

        if (Constants.SELECTED_TAB_STATE == Constants.DAILY) {
            // Set to start of the day
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date start = calendar.getTime();
            Date end = new Date(start.getTime() + 24 * 60 * 60 * 1000); // Add 1 day

            transaction = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", start)
                    .lessThan("date", end)
                    .equalTo("type", type)
                    .findAll();

        } else if (Constants.SELECTED_TAB_STATE == Constants.MONTHLY) {
            // Set to first day of the month
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1); // Move to next month
            Date endTime = calendar.getTime();

            transaction = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", type)
                    .findAll();
        }

        // Update LiveData
        categoryTransactions.setValue(null);
        categoryTransactions.setValue(transaction);
    }

    /**
     * Fetches all transactions and calculates income, expense, and total based on
     * selected tab (DAILY or MONTHLY).
     */
    public void getTransaction(Calendar calendar) {
        this.calendar = Calendar.getInstance();
        double income = 0, expense = 0, total = 0;
        RealmResults<Transaction> transaction = null;

        if (Constants.SELECTED_TAB == Constants.DAILY) {
            // Start of day
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date start = calendar.getTime();
            Date end = new Date(start.getTime() + (24 * 60 * 60 * 1000));

            transaction = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", start)
                    .lessThan("date", end)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", start)
                    .lessThan("date", end)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", start)
                    .lessThan("date", end)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", start)
                    .lessThan("date", end)
                    .sum("amount").doubleValue();

        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            // Set to 1st of the month
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            Date endTime = calendar.getTime();

            transaction = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date",endTime)
                    .sum("amount").doubleValue();
        }

        // Update LiveData
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        this.total.setValue(total);
        this.transaction.setValue(transaction);
    }

    /**
     * Adds or updates a transaction in the Realm database.
     */
    public void addTransaction(Transaction transaction) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);
        realm.commitTransaction();
    }

    /**
     * Deletes a transaction from the database and refreshes transaction data.
     */
    public void deleteTransaction(Transaction transaction) {
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransaction(calendar); // Refresh data after deletion
    }
}
