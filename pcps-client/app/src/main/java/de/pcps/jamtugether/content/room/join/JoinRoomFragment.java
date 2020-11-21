package de.pcps.jamtugether.content.room.join;

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
import de.pcps.jamtugether.databinding.FragmentJoinRoomBinding;

public class JoinRoomFragment extends Fragment {

    @Inject
    NavigationManager navigationManager;

    private JoinRoomViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);
        viewModel = new ViewModelProvider(this).get(JoinRoomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJoinRoomBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join_room, container, false);
        binding.setViewModel(viewModel);

        viewModel.getNavigateToNormalRoom().observe(getViewLifecycleOwner(), navigateToNormalRoom -> {
            if(navigateToNormalRoom) {
                navigationManager.navigateToNormalRoomFragment(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToNormalRoom();
            }
        });

        return binding.getRoot();
    }
}
