package de.pcps.jamtugether.content.menu.create;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentCreateRoomBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class CreateRoomFragment extends BaseFragment {

    private Activity activity;

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
            if(networkError != null) {
                AlertDialog dialog = UiUtils.createInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                dialog.setOnShowListener(arg -> {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
                    viewModel.onNetworkErrorShown();
                });
                dialog.show();
            }
        });

        viewModel.getNavigateToAdminRoom().observe(getViewLifecycleOwner(), navigateToJamRoom -> {
            if(navigateToJamRoom) {
                NavigationUtils.navigateToAdminRoom(NavHostFragment.findNavController(this), viewModel.getRoomID(), viewModel.getPassword(), viewModel.getToken());
                UiUtils.hideKeyboard(activity, binding.getRoot());
                viewModel.onNavigatedToAdminRoom();
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
