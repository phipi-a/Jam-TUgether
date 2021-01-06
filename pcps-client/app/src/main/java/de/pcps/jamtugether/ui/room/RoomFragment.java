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

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.TabLayoutAdapter;
import de.pcps.jamtugether.ui.base.TabLayoutFragment;
import de.pcps.jamtugether.ui.base.views.JamTabView;
import de.pcps.jamtugether.ui.room.music.MusicianViewFragment;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewFragment;
import de.pcps.jamtugether.utils.UiUtils;

public class RoomFragment extends TabLayoutFragment {

    private int roomID;
    private int userID;

    private String password;

    private String token;

    private boolean userIsAdmin;

    private RoomViewModel roomViewModel;

    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            RoomFragmentArgs args = RoomFragmentArgs.fromBundle(getArguments());
            this.roomID = args.getRoomID();
            this.userID = args.getUserID();
            this.password = args.getPassword();
            this.token = args.getToken();
            this.userIsAdmin = args.getAdmin();

            RoomViewModel.Factory roomViewModelFactory = new RoomViewModel.Factory(roomID, token, userIsAdmin);
            roomViewModel = new ViewModelProvider(this, roomViewModelFactory).get(RoomViewModel.class);

            String currentToken = roomViewModel.getToken().getValue();
            compositeSoundtrackViewModel = new ViewModelProvider(this, new CompositeSoundtrackViewModel.Factory(roomID, userID, currentToken)).get(CompositeSoundtrackViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        roomViewModel.getToken().observe(getViewLifecycleOwner(), token -> compositeSoundtrackViewModel.onTokenChanged(token));

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

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? SoundtrackOverviewFragment.newInstance(roomID, userID, password, token, userIsAdmin) : MusicianViewFragment.newInstance(roomID, userID, token);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
    }
}
