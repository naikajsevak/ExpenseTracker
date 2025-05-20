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

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>{
    private Context context;
    private RealmResults<Transaction> transaction;
    public TransactionsAdapter(Context context, RealmResults<Transaction> transaction)
    {
        this.context=context;
        this.transaction = transaction;
    }
    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        Transaction transaction = this.transaction.get(position);
        holder.binding.amt.setText(String.valueOf(transaction.getAmount()));
        holder.binding.account.setText(transaction.getAccount());
        holder.binding.transactionDate.setText(Helper.formateDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());
        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.binding.transactionImage.setImageResource(transactionCategory.getCategoryIcon());
        holder.binding.transactionImage.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));
        holder.binding.account.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));
        if(transaction.getType().equals(Constants.INCOME)){
            holder.binding.amt.setTextColor(context.getColor(R.color.green));
        }
        else if(transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.amt.setTextColor(context.getColor(R.color.red));
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure to delete  this transaction?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity)context).mainViewModel.deleteTransaction(transaction);
                    }
                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.dismiss();
                    }
                });
                deleteDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaction.size();
    }

    public class TransactionsViewHolder extends RecyclerView.ViewHolder {
        RowTransactionBinding binding;
        public TransactionsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
