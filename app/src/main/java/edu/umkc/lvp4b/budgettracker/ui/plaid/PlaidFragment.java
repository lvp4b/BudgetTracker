package edu.umkc.lvp4b.budgettracker.ui.plaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plaid.link.Plaid;
import com.plaid.linkbase.models.configuration.LinkConfiguration;
import com.plaid.linkbase.models.configuration.PlaidEnvironment;
import com.plaid.linkbase.models.configuration.PlaidOptions;
import com.plaid.linkbase.models.configuration.PlaidProduct;
import com.plaid.linkbase.models.connection.PlaidLinkResultHandler;
import com.plaid.log.LogLevel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentLineItemsBinding;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentPlaidBinding;
import edu.umkc.lvp4b.budgettracker.databinding.LineItemBinding;
import edu.umkc.lvp4b.budgettracker.databinding.PlaidBinding;
import edu.umkc.lvp4b.budgettracker.ui.transaction.AddLineItemFragment;
import edu.umkc.lvp4b.budgettracker.ui.transaction.LineItem;
import edu.umkc.lvp4b.budgettracker.ui.transaction.Transaction;
import edu.umkc.lvp4b.budgettracker.ui.transaction.TransactionsViewModel;
import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaidFragment extends Fragment {
    private Button btn;

    private final PlaidLinkResultHandler plaidResultHandler = new PlaidLinkResultHandler(
            1,
            linkConnection -> {
                btn.setVisibility(View.GONE);
                new PlaidApi.Impl().exchange(linkConnection.getPublicToken()).enqueue(new Callback<PlaidApi.ExchangeResponse>() {
                    @Override
                    public void onResponse(Call<PlaidApi.ExchangeResponse> call, Response<PlaidApi.ExchangeResponse> response) {
                        if (response.isSuccessful()){
                            System.out.println("ACCESS TOKEN: " + response.body().access_token);
                        } else {
                            try {
                                System.out.println("ACCESS TOKEN ERROR: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaidApi.ExchangeResponse> call, Throwable t) {
                        System.out.println(t);
                    }
                });

                return Unit.INSTANCE;
            },
            linkCancellation -> Unit.INSTANCE,
            plaidApiError -> Unit.INSTANCE
    );

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final PlaidBinding plaidBinding;

        public ViewHolder(@NonNull PlaidBinding plaidBinding){
            super(plaidBinding.getRoot());
            this.plaidBinding = plaidBinding;
        }

        @NonNull
        public PlaidBinding getPlaidBinding(){
            return plaidBinding;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentPlaidBinding binding = FragmentPlaidBinding.inflate(inflater);

        Plaid.setOptions(new PlaidOptions.Builder()
                .environment(PlaidEnvironment.SANDBOX)
                .logLevel(LogLevel.DEBUG)
                .build());

        binding.button.setOnClickListener(e -> {
            Plaid.openLink(this,
                    new LinkConfiguration.Builder("Budget Tracker",
                            Collections.singletonList(PlaidProduct.TRANSACTIONS))
                            .build(),1);
        });
        btn = binding.button;
        binding.button.setVisibility(View.VISIBLE);

        final PlaidViewModel plaidViewModel =
                ViewModelProviders.of(requireActivity()).get(PlaidViewModel.class);

       final TransactionsViewModel transactionsViewModel = ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.lineItem.setHasFixedSize(true);
        binding.lineItem.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lineItem.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                PlaidBinding plaidBinding = PlaidBinding.inflate(inflater, parent, false);
                plaidBinding.name.setOnClickListener(view -> {
                    LineItem lineItem = plaidBinding.getLineItem();
                    if (lineItem.getCategoryId() < 0){
                        return;
                    }

                    PlaidApi.Transaction txn = plaidViewModel.getTxns().get(lineItem.getCategoryId());
                    lineItem.setCategoryId(-lineItem.getCategoryId());

                    Transaction transaction = new Transaction(0, new Date(), txn.name, txn.amount,
                            new ArrayList<>(Collections.singleton(new LineItem())), null);
                    transactionsViewModel.addTransaction(transaction);

                    binding.lineItem.getAdapter().notifyDataSetChanged();
                });
                return new ViewHolder(plaidBinding);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.getPlaidBinding().setLineItem(plaidViewModel.getLineItems().getValue().get(position));
                holder.getPlaidBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return plaidViewModel.getLineItems().getValue().size();
            }
        });

        plaidViewModel.getLineItems().observe(this, e -> {
            binding.lineItem.getAdapter().notifyDataSetChanged();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        plaidResultHandler.onActivityResult(requestCode, resultCode, data);
    }
}
