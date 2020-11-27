package de.pcps.jamtugether.content.room.join;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.databinding.FragmentJoinRoomBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class JoinRoomFragment extends Fragment {

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
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.roomIdTextInputLayout.observeError(viewModel.getRoomInputError(), getViewLifecycleOwner());
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

        viewModel.getNavigateToRegularRoom().observe(getViewLifecycleOwner(), navigateToRegularRoom -> {
            if(navigateToRegularRoom) {
                NavigationUtils.navigateToRegularRoom(NavHostFragment.findNavController(this), viewModel.getRoomID());
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
