package de.pcps.jamtugether.content.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.utils.NavigationUtils;
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
        FragmentMenuBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        binding.setViewModel(viewModel);

        viewModel.getNavigateToSettings().observe(getViewLifecycleOwner(), navigateToSettings -> {
            if(navigateToSettings) {
                NavigationUtils.navigateToSettings(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToSettings();
            }
        });

        viewModel.getNavigateToCreateRoom().observe(getViewLifecycleOwner(), navigateToCreateRoom -> {
            if(navigateToCreateRoom) {
                NavigationUtils.navigateToCreateRoom(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToCreateRoom();
            }
        });

        viewModel.getNavigateToJoinRoom().observe(getViewLifecycleOwner(), navigateToJoinRoom -> {
            if(navigateToJoinRoom) {
                NavigationUtils.navigateToJoinRoom(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToJoinRoom();
            }
        });

        return binding.getRoot();
    }
}
