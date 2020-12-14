package de.pcps.jamtugether.content.menu.join;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.content.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentJoinRoomBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class JoinRoomFragment extends BaseFragment {

    private Activity activity;

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

        binding.roomIdTextInputLayout.observeError(viewModel.getRoomInputError(), getViewLifecycleOwner());
        binding.roomPasswordTextInputLayout.observeError(viewModel.getPasswordInputError(), getViewLifecycleOwner());

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getNavigateToRegularRoom().observe(getViewLifecycleOwner(), navigateToRegularRoom -> {
            if(navigateToRegularRoom) {
                NavigationUtils.navigateToRoomAsRegular(NavHostFragment.findNavController(this), viewModel.getRoomID(), viewModel.getToken(), viewModel.getPassword());
                UiUtils.hideKeyboard(activity, binding.getRoot());
                viewModel.onNavigatedToRegularRoom();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}