package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentImageBinding;

public class ImageFragment extends Fragment {

    private FragmentImageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater);

        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.setTransaction(transactionsViewModel.getSelectedTransaction());

        binding.capture.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_edit_transaction_to_camera);
        });

        binding.imageView.setImageBitmap(binding.getTransaction().getImage());

        return binding.getRoot();
    }
}
