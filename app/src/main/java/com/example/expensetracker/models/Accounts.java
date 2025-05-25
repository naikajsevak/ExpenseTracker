package com.example.expensetracker.models;

/**
 * Model class representing a financial account.
 * Contains information about the account name and its current amount/balance.
 */
public class Accounts {

    private double accountAmount;   // Current balance or amount in the account
    private String accountName;     // Name or identifier of the account

    /**
     * Default constructor required for some serialization/deserialization libraries.
     */
    public Accounts() {
        // No-argument constructor
    }

    /**
     * Parameterized constructor to create an account instance with initial values.
     *
     * @param accountAmount Initial balance of the account
     * @param accountName   Name or type of the account
     */
    public Accounts(double accountAmount, String accountName) {
        this.accountAmount = accountAmount;
        this.accountName = accountName;
    }

    /**
     * Getter for account amount.
     *
     * @return Current balance of the account
     */
    public double getAccountAmount() {
        return accountAmount;
    }

    /**
     * Setter for account amount.
     *
     * @param accountAmount New balance to set for the account
     */
    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    /**
     * Getter for account name.
     *
     * @return Name of the account
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Setter for account name.
     *
     * @param accountName New name or label for the account
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
