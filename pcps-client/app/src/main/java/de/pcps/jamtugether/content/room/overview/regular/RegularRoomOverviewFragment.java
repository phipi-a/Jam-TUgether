package de.pcps.jamtugether.content.room.overview.regular;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.room.overview.RoomOverviewFragment;
import de.pcps.jamtugether.content.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;

public class RegularRoomOverviewFragment extends RoomOverviewFragment {

    @NonNull
    public static RegularRoomOverviewFragment newInstance(int roomID, @NonNull String token) {
        RegularRoomOverviewFragment fragment = new RegularRoomOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegularRoomOverviewViewModel.Factory viewModelFactory = new RegularRoomOverviewViewModel.Factory(roomID, token);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(RegularRoomOverviewViewModel.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected SoundtrackListAdapter createSoundtrackListAdapter() {
        return new RegularSoundtrackListAdapter(viewModel, getViewLifecycleOwner());
    }
}
