package de.pcps.jamtugether.content.room.overview.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.room.overview.RoomOverviewFragment;
import de.pcps.jamtugether.content.soundtrack.adapters.AdminSoundtrackListAdapter;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;

public class AdminRoomOverviewFragment extends RoomOverviewFragment {

    private static final String PASSWORD_KEY = "password_key";

    private String password;

    @NonNull
    public static AdminRoomOverviewFragment newInstance(int roomID, @NonNull String password, @NonNull String token) {
        AdminRoomOverviewFragment fragment = new AdminRoomOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putString(PASSWORD_KEY, password);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.password = getArguments().getString(PASSWORD_KEY);
        }
        AdminRoomOverviewViewModel.Factory viewModelFactory = new AdminRoomOverviewViewModel.Factory(roomID, password, token);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AdminRoomOverviewViewModel.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected SoundtrackListAdapter createSoundtrackListAdapter() {
        return new AdminSoundtrackListAdapter(viewModel, (AdminRoomOverviewViewModel) viewModel, getViewLifecycleOwner());
    }
}
