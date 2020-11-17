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

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.base.navigation.NavigationManager;
import de.pcps.jamtugether.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {

    @Inject
    NavigationManager navigationManager;

    private MenuViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMenuBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        binding.setViewModel(viewModel);

        viewModel.getNavigateToCreateRoom().observe(getViewLifecycleOwner(), navigateToCreateRoom -> {
            if(navigateToCreateRoom) {
                navigationManager.navigateToCreateRoom(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToCreateRoom();
            }
        });

        viewModel.getNavigateToJoinRoom().observe(getViewLifecycleOwner(), navigateToJoinRoom -> {
            if(navigateToJoinRoom) {
                navigationManager.navigateToJoinRoom(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToJoinRoom();
            }
        });

        return binding.getRoot();
    }
}
