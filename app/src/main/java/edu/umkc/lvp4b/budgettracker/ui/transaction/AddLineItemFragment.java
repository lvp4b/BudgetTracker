package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentAddLineItemBinding;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentAddTransactionBinding;

public class AddLineItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentAddLineItemBinding binding = FragmentAddLineItemBinding.inflate(inflater);
        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);
        binding.setTransaction(transactionsViewModel.getSelectedTransaction());

        int lineItemIndex = getArguments().getInt("lineItem");
        binding.setLineItem(lineItemIndex < 0 ? new LineItem() :
                binding.getTransaction().getLineItems().get(lineItemIndex));

        binding.submit.setText(lineItemIndex < 0 ? "Add" : "Save");
        binding.submit.setOnClickListener(view -> {
            if (lineItemIndex < 0) {
                binding.getTransaction().addLineItem(binding.getLineItem());
            }

            transactionsViewModel.updateTransaction(binding.getTransaction());
            Navigation.findNavController(view).navigateUp();
        });

        binding.delete.setVisibility(lineItemIndex < 0 ? View.INVISIBLE : View.VISIBLE);
        binding.delete.setOnClickListener(view -> {
            binding.getTransaction().removeLineItem(lineItemIndex);
            transactionsViewModel.updateTransaction(binding.getTransaction());
            Navigation.findNavController(view).navigateUp();
        });

        return binding.getRoot();
    }

    public static Bundle newInstance(int lineItemIndex) {
        Bundle args = new Bundle();
        args.putInt("lineItem", lineItemIndex);
        return args;
    }
}
