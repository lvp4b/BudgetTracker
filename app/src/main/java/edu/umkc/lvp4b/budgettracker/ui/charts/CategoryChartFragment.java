package edu.umkc.lvp4b.budgettracker.ui.charts;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentCategoryChartBinding;
import edu.umkc.lvp4b.budgettracker.ui.categories.CategoriesViewModel;
import edu.umkc.lvp4b.budgettracker.ui.categories.Category;
import edu.umkc.lvp4b.budgettracker.ui.transaction.LineItem;
import edu.umkc.lvp4b.budgettracker.ui.transaction.Transaction;
import edu.umkc.lvp4b.budgettracker.ui.transaction.TransactionsViewModel;

public class CategoryChartFragment extends Fragment {
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoriesViewModel categoriesViewModel = ViewModelProviders.of(requireActivity()).get(CategoriesViewModel.class);
        TransactionsViewModel transactionsViewModel = ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        FragmentCategoryChartBinding binding = FragmentCategoryChartBinding.inflate(inflater);

        Map<Integer, Category> categoriesById = categoriesViewModel.getCategories().getValue().stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        Observer<List<Transaction>> updatePieChart = (transactions) ->
            binding.getRoot().<PieChartView>findViewById(R.id.pieChart).setSlices(
                    transactions.stream().flatMap(transaction -> transaction.getLineItems().stream())
                            .collect(Collectors.groupingBy(LineItem::getCategoryId)).entrySet().stream()
                            .map(entry -> Pair.create(categoriesById.get(entry.getKey()), entry.getValue()))
                            .filter(pair -> pair.first != null && !pair.first.isIncome())
                            .map(pair -> new PieChartView.Slice(pair.second.stream().mapToDouble(LineItem::getAmount).sum(), pair.first.getName()))
                            .collect(Collectors.toList()));

        transactionsViewModel.getTransactions().observe(this, updatePieChart);

        return binding.getRoot();
    }
}
