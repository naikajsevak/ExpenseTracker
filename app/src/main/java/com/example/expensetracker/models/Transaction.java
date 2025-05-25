package com.example.expensetracker.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm model class representing a financial transaction.
 * Each transaction contains information about its type, category, associated account,
 * note/description, date, amount, and a unique identifier.
 * This class extends RealmObject for persistence with Realm database.
 */
public class Transaction extends RealmObject {

    private String type;        // Transaction type, e.g., "Income" or "Expense"
    private String category;    // Category name for the transaction (e.g., Food, Rent)
    private String account;     // Account involved in the transaction (e.g., Cash, Bank)
    private String note;        // Optional note or description for the transaction
    private Date date;          // Date when the transaction occurred
    private double amount;      // Transaction amount
    @PrimaryKey
    private long id;            // Unique identifier for each transaction record

    /**
     * Default no-argument constructor required by Realm.
     */
    public Transaction() {}

    /**
     * Parameterized constructor to initialize all fields of a transaction.
     *
     * @param type     Type of the transaction (Income/Expense)
     * @param category Category of the transaction
     * @param account  Account involved in the transaction
     * @param note     Optional descriptive note
     * @param date     Date of the transaction
     * @param amount   Monetary amount
     * @param id       Unique transaction ID (primary key)
     */
    public Transaction(String type, String category, String account, String note, Date date, double amount, long id) {
        this.type = type;
        this.category = category;
        this.account = account;
        this.note = note;
        this.date = date;
        this.amount = amount;
        this.id = id;
    }

    /** Getter for transaction type */
    public String getType() {
        return type;
    }

    /** Setter for transaction type */
    public void setType(String type) {
        this.type = type;
    }

    /** Getter for transaction category */
    public String getCategory() {
        return category;
    }

    /** Setter for transaction category */
    public void setCategory(String category) {
        this.category = category;
    }

    /** Getter for account associated with transaction */
    public String getAccount() {
        return account;
    }

    /** Setter for account */
    public void setAccount(String account) {
        this.account = account;
    }

    /** Getter for optional transaction note */
    public String getNote() {
        return note;
    }

    /** Setter for transaction note */
    public void setNote(String note) {
        this.note = note;
    }

    /** Getter for transaction date */
    public Date getDate() {
        return date;
    }

    /** Setter for transaction date */
    public void setDate(Date date) {
        this.date = date;
    }

    /** Getter for transaction amount */
    public double getAmount() {
        return amount;
    }

    /** Setter for transaction amount */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /** Getter for unique transaction ID */
    public long getId() {
        return id;
    }

    /** Setter for unique transaction ID */
    public void setId(long id) {
        this.id = id;
    }
}
