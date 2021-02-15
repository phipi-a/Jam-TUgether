package de.pcps.jamtugether.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.databinding.FragmentSettingsBinding;
import de.pcps.jamtugether.utils.NavigationUtils;

public class SettingsFragment extends BaseFragment {

    private SettingsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        viewModel.getNavigateToOnBoarding().observe(getViewLifecycleOwner(), navigateToOnBoarding -> {
            if (navigateToOnBoarding) {
                NavigationUtils.navigateToOnBoarding(getNavController());
                viewModel.onNavigatedToOnBoarding();
            }
        });
        return binding.getRoot();
    }
}
