package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.data.AppDatabase;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentTransactionBinding;
import edu.umkc.lvp4b.budgettracker.databinding.TransactionBinding;
import edu.umkc.lvp4b.budgettracker.ui.categories.CategoriesViewModel;

public class TransactionFragment extends Fragment {

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TransactionBinding transactionBinding;

        public ViewHolder(@NonNull TransactionBinding transactionBinding){
            super(transactionBinding.getRoot());
            this.transactionBinding = transactionBinding;
        }

        @NonNull
        public TransactionBinding getTransactionBinding(){
            return transactionBinding;
        }
    }

    private TransactionsViewModel transactionsViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionsViewModel = ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        ViewModelProviders.of(requireActivity()).get(CategoriesViewModel.class);

        FragmentTransactionBinding binding = FragmentTransactionBinding.inflate(inflater);
        binding.setViewmodel(transactionsViewModel);

        MutableLiveData<Observer<List<Transaction>>> init = new MutableLiveData<>();
        init.setValue(transactions -> {
            binding.recycler.getAdapter().notifyDataSetChanged();
            if (!transactions.isEmpty())
            {
                transactionsViewModel.getTransactions().removeObserver(init.getValue());
            }
        });
        transactionsViewModel.getTransactions().observe(this, init.getValue());

        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TransactionBinding transactionBinding = TransactionBinding.inflate(inflater, parent, false);
                transactionBinding.name.setOnClickListener(view -> {
                    transactionsViewModel.setSelectedTransaction(transactionBinding.getTransaction());
                    Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_edit_transaction);
                });
                return new ViewHolder(transactionBinding);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.getTransactionBinding().setTransaction(transactionsViewModel.getTransactions().getValue().get(position));
                holder.getTransactionBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return transactionsViewModel.getTransactions().getValue().size();
            }
        });

        binding.add.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_add_transaction));

        return binding.getRoot();
    }
}
