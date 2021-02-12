package de.pcps.jamtugether.ui.room.overview.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentAdminSettingsBinding;
import de.pcps.jamtugether.ui.base.BaseDialogFragment;
import de.pcps.jamtugether.ui.base.views.dialogs.ConfirmationDialog;
import de.pcps.jamtugether.ui.room.RoomFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.utils.UiUtils;

public class AdminSettingsFragment extends BaseDialogFragment {

    private AdminSettingsViewModel viewModel;

    @NonNull
    public static AdminSettingsFragment newInstance() {
        return new AdminSettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AdminSettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAdminSettingsBinding binding = FragmentAdminSettingsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getShowRoomDeletionConfirmDialog().observe(getViewLifecycleOwner(), showRoomDeletionConfirmDialog -> {
            if (showRoomDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(context, R.string.delete_room, R.string.delete_room_confirmation, new ConfirmationDialog.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        viewModel.onRoomDeletionConfirmButtonClicked();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                    }
                });
                viewModel.onRoomDeletionConfirmDialogShown();
            }
        });

        viewModel.getNavigateBack().observe(getViewLifecycleOwner(), navigateBack -> {
            if (navigateBack) {
                if (getParentFragment() != null && getParentFragment().getParentFragment() instanceof RoomFragment) {
                    RoomFragment roomFragment = (RoomFragment) getParentFragment().getParentFragment();
                    NavigationUtils.navigateBack(roomFragment.getNavController());
                    viewModel.onNavigatedBack();
                }
            }
        });

        return binding.getRoot();
    }
}
