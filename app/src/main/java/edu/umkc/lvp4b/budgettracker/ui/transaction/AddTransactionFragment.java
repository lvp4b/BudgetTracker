package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.stream.Stream;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentAddTransactionBinding;
import edu.umkc.lvp4b.budgettracker.ui.categories.CategoriesViewModel;
import edu.umkc.lvp4b.budgettracker.ui.categories.Category;

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

        final CategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(requireActivity()).get(CategoriesViewModel.class);

        final ArrayAdapter<Category> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                Stream.concat(Stream.of(Category.UNASSIGNED),
                categoriesViewModel.getCategories().getValue().stream()).toArray(Category[]::new));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.category.setAdapter(adapter);

        final int categoryId = binding.getTransaction().getLineItems().get(0).getCategoryId();
        for (int i = 0; i != adapter.getCount(); i++){
            Category category = adapter.getItem(i);
            if (category.getId() == categoryId){
                binding.category.setSelection(i);
                break;
            }
        }

        binding.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.getTransaction().getLineItems().get(0).setCategoryId(((Category) parent.getItemAtPosition(position)).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.getTransaction().getLineItems().get(0).setCategoryId(0);
            }
        });

        return binding.getRoot();
    }
}
