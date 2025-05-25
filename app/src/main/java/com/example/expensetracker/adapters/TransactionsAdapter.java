package com.example.expensetracker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.RowTransactionBinding;
import com.example.expensetracker.models.Category;
import com.example.expensetracker.models.Transaction;
import com.example.expensetracker.utils.Constants;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodel.MainViewModel;
import com.example.expensetracker.views.activities.MainActivity;

import io.realm.RealmResults;

/**
 * RecyclerView Adapter to display a list of transactions.
 * Each transaction shows amount, account, date, category, and related icons.
 * Supports long press to delete a transaction with confirmation dialog.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder> {

    private Context context;                   // Context for accessing resources and UI operations
    private RealmResults<Transaction> transaction;  // List of transactions fetched from Realm database

    /**
     * Constructor to initialize adapter with context and transactions list.
     *
     * @param context     Activity or fragment context
     * @param transaction RealmResults list of Transaction objects
     */
    public TransactionsAdapter(Context context, RealmResults<Transaction> transaction) {
        this.context = context;
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row in RecyclerView using ViewBinding
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false);
        return new TransactionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        // Get the transaction at the current position
        Transaction transaction = this.transaction.get(position);

        // Bind transaction data to the respective views
        holder.binding.amt.setText(String.valueOf(transaction.getAmount()));
        holder.binding.account.setText(transaction.getAccount());
        holder.binding.transactionDate.setText(Helper.formateDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        // Fetch category details to set icon and color accordingly
        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());
        holder.binding.transactionImage.setImageResource(transactionCategory.getCategoryIcon());
        holder.binding.transactionImage.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

        // Set background tint for account label based on account type
        holder.binding.account.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));

        // Set amount text color based on transaction type (Income/Expense)
        if (transaction.getType().equals(Constants.INCOME)) {
            holder.binding.amt.setTextColor(context.getColor(R.color.green));
        } else if (transaction.getType().equals(Constants.EXPENSE)) {
            holder.binding.amt.setTextColor(context.getColor(R.color.red));
        }

        // Handle long press on a transaction item to show delete confirmation dialog
        holder.itemView.setOnLongClickListener(view -> {
            AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
            deleteDialog.setTitle("Delete Transaction");
            deleteDialog.setMessage("Are you sure to delete this transaction?");
            deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
                // Call ViewModel method to delete the selected transaction
                ((MainActivity) context).mainViewModel.deleteTransaction(transaction);
            });
            deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> {
                // Dismiss the dialog without deleting
                deleteDialog.dismiss();
            });
            deleteDialog.show();
            return true;  // Indicate that the long click was handled
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of transactions available
        return transaction.size();
    }

    /**
     * ViewHolder class to hold and recycle views for transaction items.
     */
    public class TransactionsViewHolder extends RecyclerView.ViewHolder {
        RowTransactionBinding binding;

        public TransactionsViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind views using ViewBinding for better type safety and readability
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
