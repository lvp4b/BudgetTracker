package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentLineItemsBinding;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentUpdateTransactionBinding;
import edu.umkc.lvp4b.budgettracker.databinding.LineItemBinding;
import edu.umkc.lvp4b.budgettracker.databinding.TransactionBinding;

public class LineItemsFragment extends Fragment {

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final LineItemBinding lineItemBinding;

        public ViewHolder(@NonNull LineItemBinding lineItemBinding){
            super(lineItemBinding.getRoot());
            this.lineItemBinding = lineItemBinding;
        }

        @NonNull
        public LineItemBinding getLineItemBinding(){
            return lineItemBinding;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentLineItemsBinding binding = FragmentLineItemsBinding.inflate(inflater);

        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.setTransaction(transactionsViewModel.getSelectedTransaction());

        binding.lineItem.setHasFixedSize(true);
        binding.lineItem.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lineItem.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LineItemBinding lineItemBinding = LineItemBinding.inflate(inflater, parent, false);
                lineItemBinding.name.setOnClickListener(view -> {
                    Navigation.findNavController(view).navigate(R.id.action_edit_transaction_to_add_line_item,
                            AddLineItemFragment.newInstance(transactionsViewModel.getSelectedTransaction()
                            .getLineItems().indexOf(lineItemBinding.getLineItem())));
                });
                return new ViewHolder(lineItemBinding);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.getLineItemBinding().setLineItem(transactionsViewModel.getSelectedTransaction().getLineItems().get(position + 1));
                holder.getLineItemBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return transactionsViewModel.getSelectedTransaction().getLineItems().size() - 1;
            }
        });

        binding.add.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_edit_transaction_to_add_line_item,
                        AddLineItemFragment.newInstance(-1)));

        return binding.getRoot();
    }
}
