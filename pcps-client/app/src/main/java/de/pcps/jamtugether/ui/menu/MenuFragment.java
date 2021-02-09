package de.pcps.jamtugether.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentMenuBinding;

public class MenuFragment extends BaseFragment {

    private MenuViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        viewModel.getNavigateToSettings().observe(getViewLifecycleOwner(), navigateToSettings -> {
            if (navigateToSettings) {
                NavigationUtils.navigateToSettings(getNavController());
                viewModel.onNavigatedToSettings();
            }
        });

        viewModel.getNavigateToCreateRoom().observe(getViewLifecycleOwner(), navigateToCreateRoom -> {
            if (navigateToCreateRoom) {
                NavigationUtils.navigateToCreateRoom(getNavController());
                viewModel.onNavigatedToCreateRoom();
            }
        });

        viewModel.getNavigateToJoinRoom().observe(getViewLifecycleOwner(), navigateToJoinRoom -> {
            if (navigateToJoinRoom) {
                NavigationUtils.navigateToJoinRoom(getNavController());
                viewModel.onNavigatedToJoinRoom();
            }
        });

        return binding.getRoot();
    }
}
