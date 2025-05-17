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
import com.example.expensetracker.models.Category;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountsViewholder>{
    private Context context;
    private ArrayList<Accounts> accountsArrayList;
    public interface AccountClickListener{
        void onAccountClick(Accounts accounts);
    }
    AccountClickListener accountClickListener;
    public AccountAdapter(Context context, ArrayList<Accounts> accountsArrayList,AccountClickListener accountClickListener){
        this.context=context;
        this.accountsArrayList=accountsArrayList;
        this.accountClickListener=accountClickListener;
    }
    @NonNull
    @Override
    public AccountsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountsViewholder(LayoutInflater.from(context).inflate(R.layout.row_accounts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewholder holder, int position) {
        Accounts accounts = accountsArrayList.get(position);
        holder.binding.accountName.setText(accounts.getAccountName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountClickListener.onAccountClick(accounts);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountsArrayList.size();
    }

    public class AccountsViewholder extends RecyclerView.ViewHolder{
        RowAccountsBinding binding;
        public AccountsViewholder(@NonNull View itemView) {
            super(itemView);
            binding = RowAccountsBinding.bind(itemView);
        }
    }
}
