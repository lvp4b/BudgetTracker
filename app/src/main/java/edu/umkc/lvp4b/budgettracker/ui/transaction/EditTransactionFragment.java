package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import edu.umkc.lvp4b.budgettracker.databinding.FragmentEditTransactionBinding;

public class EditTransactionFragment extends Fragment {
    private FragmentEditTransactionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditTransactionBinding.inflate(inflater);

        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.setTransaction(transactionsViewModel.getSelectedTransaction());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.viewPager.setAdapter(new TabFragmentManager(getChildFragmentManager()));
    }

    private static class TabFragmentManager extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        TabFragmentManager(FragmentManager fragmentManager) {
            super(fragmentManager);

            fragments = new Fragment[]{
                    new UpdateTransactionFragment(),
                    new LineItemsFragment(),
                    new ImageFragment()
            };
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return new String[]{"Transaction", "Line Items", "Image"}[position];
        }
    }
}
