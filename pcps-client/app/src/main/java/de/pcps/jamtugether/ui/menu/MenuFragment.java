package de.pcps.jamtugether.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentMenuBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class MenuFragment extends BaseFragment {

    private MenuViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MenuFragmentArgs args = MenuFragmentArgs.fromBundle(getArguments());
            int errorMessage=args.getErrorMessage();
            MenuViewModel.Factory menuViewModelFactory = new MenuViewModel.Factory(errorMessage);
            viewModel = new ViewModelProvider(this,menuViewModelFactory).get(MenuViewModel.class);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        viewModel.getNavigateToSettings().observe(getViewLifecycleOwner(), navigateToSettings -> {
            if (navigateToSettings) {
                NavigationUtils.navigateToSettings(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToSettings();
            }
        });
        viewModel.getShowErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage!=-1) {
                UiUtils.showSnackbar(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG);
                viewModel.onErrorMessageSnackbarShown();
            }
        });

        viewModel.getNavigateToCreateRoom().observe(getViewLifecycleOwner(), navigateToCreateRoom -> {
            if (navigateToCreateRoom) {
                NavigationUtils.navigateToCreateRoom(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToCreateRoom();
            }
        });

        viewModel.getNavigateToJoinRoom().observe(getViewLifecycleOwner(), navigateToJoinRoom -> {
            if (navigateToJoinRoom) {
                NavigationUtils.navigateToJoinRoom(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToJoinRoom();
            }
        });

        return binding.getRoot();
    }
}
