package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.databinding.FragmentShakerBinding;

public class ShakerFragment extends BaseFragment {

    private ShakerViewModel viewModel;

    @NonNull
    public static ShakerFragment newInstance() {
        return new ShakerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ShakerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentShakerBinding binding = FragmentShakerBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }
}
