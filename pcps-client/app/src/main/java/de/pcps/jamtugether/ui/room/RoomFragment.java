package de.pcps.jamtugether.ui.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentRoomBinding;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.base.views.dialogs.ConfirmationDialog;
import de.pcps.jamtugether.ui.base.views.tab.JamTabLayout;
import de.pcps.jamtugether.utils.UiUtils;

public class RoomFragment extends BaseFragment {

    private JamTabLayout tabLayout;

    private RoomViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            RoomFragmentArgs args = RoomFragmentArgs.fromBundle(getArguments());
            int roomID = args.getRoomID();
            User user = args.getUser();
            String password = args.getPassword();
            String token = args.getToken();
            boolean userIsAdmin = args.getAdmin();

            RoomViewModel.Factory roomViewModelFactory = new RoomViewModel.Factory(roomID, password, user, token, userIsAdmin);
            viewModel = new ViewModelProvider(this, roomViewModelFactory).get(RoomViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRoomBinding binding = FragmentRoomBinding.inflate(inflater, container, false);

        tabLayout = binding.tabLayout;
        tabLayout.setup(binding.viewPager, new RoomAdapter(this, viewModel, tabLayout));

        viewModel.observeAdminStatus(getViewLifecycleOwner());
        viewModel.observeRoomDeletion(getViewLifecycleOwner());

        viewModel.getShowLeaveRoomConfirmationDialog().observe(getViewLifecycleOwner(), showLeaveRoomConfirmationDialog -> {
            if (showLeaveRoomConfirmationDialog) {
                UiUtils.showConfirmationDialog(context, R.string.leave_room, R.string.leave_room_confirmation, new ConfirmationDialog.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        viewModel.onLeaveRoomConfirmationButtonClicked();
                    }

                    @Override
                    public void onNegativeButtonClicked() { }
                });
                viewModel.onLeaveRoomConfirmationDialogShown();
            }
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getShowUserBecameAdminSnackbar().observe(getViewLifecycleOwner(), showUserBecameAdminSnackbar -> {
            if (showUserBecameAdminSnackbar) {
                UiUtils.showSnackbar(binding.getRoot(), R.string.user_became_admin_snackbar_message, Snackbar.LENGTH_SHORT);
                viewModel.onUserBecameAdminSnackbarShown();
            }
        });

        viewModel.getShowUserBecameRegularSnackbar().observe(getViewLifecycleOwner(), showUserBecameRegularSnackbar -> {
            if (showUserBecameRegularSnackbar) {
                UiUtils.showSnackbar(binding.getRoot(), R.string.user_became_regular_snackbar_message, Snackbar.LENGTH_SHORT);
                viewModel.onUserBecameRegularSnackbarShown();
            }
        });

        viewModel.getShowRoomDeletedSnackbar().observe(getViewLifecycleOwner(), showSnackbar -> {
            if (showSnackbar) {
                UiUtils.showSnackbar(binding.getRoot(), R.string.room_deleted_error_message, Snackbar.LENGTH_LONG);
            }
        });

        viewModel.getNavigateBack().observe(getViewLifecycleOwner(), navigateBack -> {
            if (navigateBack) {
                this.navigateBack();
                viewModel.onNavigatedBack();
            }
        });

        return binding.getRoot();
    }

    @Override
    protected void onBackPressed() {
        viewModel.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tabLayout != null) {
            tabLayout.unregisterOnPageChangeCallback();
        }
    }
}
