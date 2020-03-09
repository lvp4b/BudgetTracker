package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentAddTransactionBinding;
public class AddTransactionFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final FragmentAddTransactionBinding binding = FragmentAddTransactionBinding.inflate(inflater);
        binding.setTransaction(new Transaction());

        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.add.setOnClickListener(view -> {
            transactionsViewModel.addTransaction(binding.getTransaction());
            Navigation.findNavController(view).navigateUp();
        });

        return binding.getRoot();
    }
}
