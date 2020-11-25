package de.pcps.jamtugether.content.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {

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
            if(navigateToSettings) {
                NavigationUtils.navigateToSettings(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToSettings();
            }
        });

        viewModel.getNavigateToCreateRoom().observe(getViewLifecycleOwner(), navigateToCreateRoom -> {
            if(navigateToCreateRoom) {
                NavigationUtils.navigateToCreateRoom(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToCreateRoom();
            }
        });

        viewModel.getNavigateToJoinRoom().observe(getViewLifecycleOwner(), navigateToJoinRoom -> {
            if(navigateToJoinRoom) {
                NavigationUtils.navigateToJoinRoom(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToJoinRoom();
            }
        });

        return binding.getRoot();
    }
}
