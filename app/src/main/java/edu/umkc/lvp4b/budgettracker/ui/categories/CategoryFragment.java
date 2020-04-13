package edu.umkc.lvp4b.budgettracker.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentCategoriesBinding;
import edu.umkc.lvp4b.budgettracker.databinding.CategoryBinding;
import edu.umkc.lvp4b.budgettracker.ui.categories.Category;
import edu.umkc.lvp4b.budgettracker.ui.categories.CategoriesViewModel;

public class CategoryFragment extends Fragment {

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final CategoryBinding categoryBinding;

        public ViewHolder(@NonNull CategoryBinding categoryBinding){
            super(categoryBinding.getRoot());
            this.categoryBinding = categoryBinding;
        }

        @NonNull
        public CategoryBinding getCategoryBinding(){
            return categoryBinding;
        }
    }

    private CategoriesViewModel categoriesViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel = ViewModelProviders.of(requireActivity()).get(CategoriesViewModel.class);

        FragmentCategoriesBinding binding = FragmentCategoriesBinding.inflate(inflater);
        binding.setViewmodel(categoriesViewModel);

        MutableLiveData<Observer<List<Category>>> init = new MutableLiveData<>();
        init.setValue(categories -> {
            binding.recycler.getAdapter().notifyDataSetChanged();
            if (!categories.isEmpty())
            {
                categoriesViewModel.getCategories().removeObserver(init.getValue());
            }
        });
        categoriesViewModel.getCategories().observe(this, init.getValue());

        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CategoryBinding categoryBinding = CategoryBinding.inflate(inflater, parent, false);
                categoryBinding.name.setOnClickListener(view -> {
                    categoriesViewModel.setSelectedCategory(categoryBinding.getCategory());
                    Navigation.findNavController(view).navigate(R.id.action_nav_category_to_edit_category);
                });
                return new ViewHolder(categoryBinding);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.getCategoryBinding().setCategory(categoriesViewModel.getCategories().getValue().get(position));
                holder.getCategoryBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return categoriesViewModel.getCategories().getValue().size();
            }
        });

        binding.add.setOnClickListener(view -> {
            categoriesViewModel.setSelectedCategory(new Category());
            Navigation.findNavController(view).navigate(R.id.action_nav_category_to_add_category);
        });

        return binding.getRoot();
    }
}
