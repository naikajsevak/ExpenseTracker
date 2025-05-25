package com.example.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.RowAccountsBinding;
import com.example.expensetracker.models.Accounts;

import java.util.ArrayList;

/**
 * Adapter class for displaying a list of account options in a RecyclerView.
 * Used for selecting the account while adding a transaction.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountsViewholder> {

    private Context context;
    private ArrayList<Accounts> accountsArrayList;

    // Interface for handling item click events
    public interface AccountClickListener {
        void onAccountClick(Accounts accounts);
    }

    private AccountClickListener accountClickListener;

    /**
     * Constructor to initialize adapter with context, data list, and click listener.
     *
     * @param context               Context of the calling activity/fragment
     * @param accountsArrayList     List of account items to display
     * @param accountClickListener  Callback for handling item selection
     */
    public AccountAdapter(Context context, ArrayList<Accounts> accountsArrayList, AccountClickListener accountClickListener) {
        this.context = context;
        this.accountsArrayList = accountsArrayList;
        this.accountClickListener = accountClickListener;
    }

    @NonNull
    @Override
    public AccountsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the account row layout
        return new AccountsViewholder(LayoutInflater.from(context).inflate(R.layout.row_accounts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewholder holder, int position) {
        // Bind account name to TextView
        Accounts accounts = accountsArrayList.get(position);
        holder.binding.accountName.setText(accounts.getAccountName());

        // Set click listener to notify selected account
        holder.itemView.setOnClickListener(view -> accountClickListener.onAccountClick(accounts));
    }

    @Override
    public int getItemCount() {
        // Return the total number of accounts
        return accountsArrayList.size();
    }

    /**
     * ViewHolder class that binds the row_accounts.xml layout using ViewBinding.
     */
    public class AccountsViewholder extends RecyclerView.ViewHolder {
        RowAccountsBinding binding;

        public AccountsViewholder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountsBinding.bind(itemView);
        }
    }
}
