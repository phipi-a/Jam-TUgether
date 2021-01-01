package de.pcps.jamtugether.ui.menu.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentCreateRoomBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class CreateRoomFragment extends BaseFragment {

    private CreateRoomViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreateRoomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCreateRoomBinding binding = FragmentCreateRoomBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        binding.roomPasswordTextInputLayout.observeError(viewModel.getPasswordInputError(), getViewLifecycleOwner());

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getNavigateToAdminRoom().observe(getViewLifecycleOwner(), navigateToJamRoom -> {
            if (navigateToJamRoom) {
                String password = viewModel.getPassword();
                String token = viewModel.getToken();
                if(password == null || token == null) {
                    return;
                }
                NavigationUtils.navigateToRoomAsAdmin(NavHostFragment.findNavController(this), viewModel.getRoomID(), viewModel.getUserID(), password, token);
                UiUtils.hideKeyboard(activity, binding.getRoot());
                viewModel.onNavigatedToAdminRoom();
            }
        });

        return binding.getRoot();
    }
}
