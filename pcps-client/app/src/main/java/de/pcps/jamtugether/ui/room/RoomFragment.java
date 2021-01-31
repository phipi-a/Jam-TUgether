package de.pcps.jamtugether.ui.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.base.TabLayoutAdapter;
import de.pcps.jamtugether.ui.base.TabLayoutFragment;
import de.pcps.jamtugether.ui.base.views.JamTabView;
import de.pcps.jamtugether.ui.room.music.MusicianViewFragment;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewFragment;
import de.pcps.jamtugether.utils.UiUtils;

public class RoomFragment extends TabLayoutFragment {

    private RoomViewModel roomViewModel;

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
            roomViewModel = new ViewModelProvider(this, roomViewModelFactory).get(RoomViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        roomViewModel.getShowLeaveRoomConfirmationDialog().observe(getViewLifecycleOwner(), showLeaveRoomConfirmationDialog -> {
            if (showLeaveRoomConfirmationDialog) {
                UiUtils.showConfirmationDialog(context, R.string.leave_room, R.string.leave_room_confirmation, () -> roomViewModel.onLeaveRoomConfirmationButtonClicked());
                roomViewModel.onLeaveRoomConfirmationDialogShown();
            }
        });

        roomViewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                roomViewModel.onNetworkErrorShown();
            }
        });

        roomViewModel.getShowUserBecameAdminSnackbar().observe(getViewLifecycleOwner(), showAdminSnackbar -> {
            if (showAdminSnackbar) {
                UiUtils.showSnackbar(view, R.string.user_became_admin_snackbar_message, Snackbar.LENGTH_LONG);
            }
        });

        roomViewModel.getNavigateBack().observe(getViewLifecycleOwner(), navigateBack -> {
            if (navigateBack) {
                this.navigateBack();
                roomViewModel.onNavigatedBack();
            }
        });

        return view;
    }

    @Override
    protected void handleOnBackPressed() {
        roomViewModel.handleBackPressed();
    }

    @NonNull
    @Override
    protected TabLayoutAdapter createTabLayoutAdapter() {
        return new TabLayoutAdapter(this) {

            @NonNull
            @Override
            public JamTabView getTabView(int position) {
                return JamTabView.from(tabLayout);
            }

            @StringRes
            @Override
            public int getTabTitle(int position) {
                return position == 0 ? R.string.soundtrack_over_view : R.string.musician_view;
            }

            @Override
            public int getInitialTabPosition() {
                return roomViewModel.getInitialTabPosition();
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? SoundtrackOverviewFragment.newInstance() : MusicianViewFragment.newInstance();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
    }
}
