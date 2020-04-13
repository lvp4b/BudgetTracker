package edu.umkc.lvp4b.budgettracker.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentAddCategoryBinding;

public class AddCategoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentAddCategoryBinding binding = FragmentAddCategoryBinding.inflate(inflater);
        final CategoriesViewModel categoriesViewModel =
                ViewModelProviders.of(requireActivity()).get(CategoriesViewModel.class);
        binding.setCategory(categoriesViewModel.getSelectedCategory());

        final boolean add = Navigation.findNavController(getActivity(), getId())
                .getCurrentDestination().getId() == R.id.add_category;

        binding.submit.setText(add ? "Add" : "Save");
        binding.submit.setOnClickListener(view -> {
            if (add) {
                categoriesViewModel.addCategory(binding.getCategory());
            } else {
                categoriesViewModel.updateCategory(binding.getCategory());
            }

            Navigation.findNavController(view).navigateUp();
        });

        binding.delete.setVisibility(add ? View.INVISIBLE : View.VISIBLE);
        binding.delete.setOnClickListener(view -> {
            categoriesViewModel.deleteCategory(binding.getCategory());
            Navigation.findNavController(view).navigateUp();
        });

        return binding.getRoot();
    }
}
