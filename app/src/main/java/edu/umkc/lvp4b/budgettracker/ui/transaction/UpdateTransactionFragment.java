package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import edu.umkc.lvp4b.budgettracker.databinding.FragmentEditTransactionBinding;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentUpdateTransactionBinding;

public class UpdateTransactionFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentUpdateTransactionBinding binding = FragmentUpdateTransactionBinding.inflate(inflater);

        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.setTransaction(transactionsViewModel.getSelectedTransaction());

        binding.submit.setOnClickListener(view -> {
            transactionsViewModel.updateTransaction(transactionsViewModel.getSelectedTransaction());
            Navigation.findNavController(view).navigateUp();
        });

        binding.delete.setOnClickListener(view -> {
            transactionsViewModel.deleteTransaction(transactionsViewModel.getSelectedTransaction());
            Navigation.findNavController(view).navigateUp();
        });

        return binding.getRoot();
    }
}
