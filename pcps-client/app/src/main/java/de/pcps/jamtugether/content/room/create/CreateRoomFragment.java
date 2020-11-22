package de.pcps.jamtugether.content.room.create;

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
import de.pcps.jamtugether.databinding.FragmentCreateRoomBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class CreateRoomFragment extends Fragment {

    @Inject
    NavigationManager navigationManager;

    private CreateRoomViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);
        viewModel = new ViewModelProvider(this).get(CreateRoomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCreateRoomBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_room, container, false);
        binding.setViewModel(viewModel);

        binding.roomPasswordTextInputLayout.observeError(viewModel.getPasswordInputError(), getViewLifecycleOwner());

        viewModel.getNavigateToAdminRoom().observe(getViewLifecycleOwner(), navigateToJamRoom -> {
            if(navigateToJamRoom) {
                navigationManager.navigateToAdminRoom(Navigation.findNavController(binding.getRoot()));
                UiUtils.hideKeyboard(requireActivity(), binding.getRoot());
                viewModel.onNavigatedToAdminRoom();
            }
        });

        return binding.getRoot();
    }
}
