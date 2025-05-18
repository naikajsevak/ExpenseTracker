package com.example.expensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {
    MutableLiveData<RealmResults<Transaction>> transaction = new MutableLiveData<>();
    Realm realm;
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
    }
    void setUpDatabase()
    {

        realm = Realm.getDefaultInstance();
    }
    void getTransaction()
    {

    }
    void addTransaction()
    {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Business","Card","Business related",new Date(),500,new Date().getTime()));
        realm.commitTransaction();
        RealmResults<Transaction> transaction =realm.where(Transaction.class).findAll();
    }
}
