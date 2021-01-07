package de.pcps.jamtugether.ui.menu.join;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentJoinRoomBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class JoinRoomFragment extends BaseFragment {

    private JoinRoomViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(JoinRoomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJoinRoomBinding binding = FragmentJoinRoomBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        binding.userNameTextInputLayout.observeError(viewModel.getNameInputError(), getViewLifecycleOwner());
        binding.roomIdTextInputLayout.observeError(viewModel.getRoomInputError(), getViewLifecycleOwner());
        binding.roomPasswordTextInputLayout.observeError(viewModel.getPasswordInputError(), getViewLifecycleOwner());

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getNavigateToRegularRoom().observe(getViewLifecycleOwner(), navigateToRegularRoom -> {
            if (navigateToRegularRoom) {
                User user = viewModel.getUser();
                String password = viewModel.getPassword();
                String token = viewModel.getToken();
                if(user == null || password == null || token == null) {
                    return;
                }
                NavigationUtils.navigateToRoomAsRegular(NavHostFragment.findNavController(this), viewModel.getRoomID(), user, password, token);
                UiUtils.hideKeyboard(activity, binding.getRoot());
                viewModel.onNavigatedToRegularRoom();
            }
        });

        return binding.getRoot();
    }
}
