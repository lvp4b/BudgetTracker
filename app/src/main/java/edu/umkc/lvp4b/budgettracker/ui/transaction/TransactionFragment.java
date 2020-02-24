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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentTransactionBinding;
import edu.umkc.lvp4b.budgettracker.databinding.TransactionBinding;

public class TransactionFragment extends Fragment {

    private TransactionsViewModel transactionsViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionsViewModel = ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        FragmentTransactionBinding binding = FragmentTransactionBinding.inflate(inflater);
        binding.setViewmodel(transactionsViewModel);

        binding.list.setAdapter(new ArrayAdapter<Transaction>(requireContext(), R.layout.transaction,
                binding.getViewmodel().getTransactions().getValue()){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TransactionBinding transactionBinding = TransactionBinding.inflate(inflater);
                transactionBinding.setTransaction(transactionsViewModel.getTransactions().getValue().get(position));
                return transactionBinding.getRoot();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_add_transaction);
            }
        });

        return binding.getRoot();
    }
}
